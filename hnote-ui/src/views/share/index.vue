<template>
    <div id="share-main-container">
        <div id="share-operation">
            <span class="svg-container svg-container-star" title="star">
                <svg-icon icon-class="star" />
            </span>
            <a class="svg-container svg-container-comment" href="#comment" title="comment">
                <svg-icon icon-class="comment" />
            </a>
            <span class="svg-container svg-container-report" title="report">
                <svg-icon icon-class="report" />
            </span>
            <span class="svg-container svg-container-encryption" title="encryption">
                <svg-icon icon-class="encryption" />
            </span>
        </div>
        <div id="share-main" class="share-file">
            <div class="share-download">
                <div class="share-content-header" >
                    <div class="share-file-name-container" :title="note.title">{{note.title}}</div>
                </div>
                <div class="share-content-container markdown-body" v-html="htmlContent">  
                </div>
            </div>
        </div>
        <div id="comment-container">
            <top-gitment :options="options" id="comment" v-if="options"></top-gitment>
        </div>
        <div id="share-notice">
            <span>本文档由 <b>{{name}}</b> 分享提供，转载请联系作者</span>
        </div>
        <el-tooltip placement="top" content="GO TOP">
            <back-to-top transitionName="fade" :customStyle="myBackToTopStyle" :visibilityHeight="300" :backPosition="50"></back-to-top>
        </el-tooltip>
    </div>
</template>

<script>
import { mapGetters } from 'vuex'
import { getShareInfoByCode } from '@/api/share'
import NProgress from 'nprogress' // progress bar
import 'nprogress/nprogress.css'// progress bar style
import BackToTop from '@/components/BackToTop'

NProgress.configure({ showSpinner: false })// NProgress Configuration
const marked = require('marked');

export default {
  data() {
    return {
      code: this.$route.params.code,
      note: '',
      htmlContent: '',
      options: {
        id: this.$route.params.id, // 评论页唯一标识符
        owner: 'mstao',// github用户名
        repo: 'mstao.github.io', // 用于存放评论的仓库
        oauth: {
          client_id: '53cc2b146d10bcfc17d1', 
          client_secret: '49891d90dcf8b9edc18dcbd29b405bc988fa6a92',
        } 
      },
      myBackToTopStyle: {
        right: '50px',
        bottom: '50px',
        width: '40px',
        height: '40px',
        'border-radius': '4px',
        'line-height': '45px', // 请保持与高度一致以垂直居中 Please keep consistent with height to center vertically
        background: '#e7eaf1'// 按钮的背景颜色 The background color of the button
      }
    }
  },
  components: { BackToTop },
  created() {
    this.init()
  },
  watch: {
    // 如果路由有变化，会再次执行该方法
    '$route': 'init'
  },
  computed: {
    ...mapGetters([
    'name',
    ]),
  },
  methods: {
    init() {
        this.fetchBasicInfo()
    },
    fetchBasicInfo() {
        // start loading
        const loading = this.$loading({
            lock: true,
            text: 'Loading',
            spinner: 'el-icon-loading'
        });
        setTimeout(() => {
            loading.close();
        }, 2000);

        // start progress bar
        NProgress.start()
        this.fetchShareByCode()

        NProgress.done()
        loading.close()
    },
    fetchShareByCode() {
        console.log(this.code)
        new Promise(() => {
          getShareInfoByCode(this.code).then(response => {
                if (response.status == 200) {
                    this.note = response.data.note;
                    this.htmlContent = marked(this.note.content) 
                }
            }) 
        })
    }
  }
}
</script>

<style>
@import "../../lib/css/markdown.css";
@import "../../lib/css/md.css";
@import url('https://cdnjs.cloudflare.com/ajax/libs/highlight.js/9.12.0/styles/github.min.css');
@import url('https://cdnjs.cloudflare.com/ajax/libs/github-markdown-css/2.9.0/github-markdown.min.css');
</style>

<style>
#share-main-container {
    padding-top: 10px;
    overflow-y: auto;
    position: relative;
    width: 100%;
    display: inline-block;
    scrollbar-track-color: #f8f8f8;
    padding-bottom: 50px;
}
#share-operation {
    position: absolute;
    top: 40px;
    left: 300px;
}
#share-operation span,a {
    display: block;
    margin-top: 10px;
    cursor: pointer;
}
#share-main, #comment-container {
    background-color: #fff;
    box-shadow: 0 1px 10px 2px rgba(0,0,0,.06);
    box-sizing: border-box;
    border: 1px solid #e5e5e5;
}
.share-file {
    width: 50%;
    margin: 0 auto;
    padding: 70px 90px;
    position: relative;
    min-height: 1000px;
}

.share-content-header {
    text-align:center;
    width: 100%;
    margin: 0 auto;
    padding: 0 20px 20px 20px;
    font-size: 26px;
    font-family: 'Segoe UI', Helvetica, Arial, sans-serif;
}

#comment-container {
    width: 50%;
    margin: 0 auto;
    padding: 70px 90px;
    position: relative;
    min-height: 200px;
}
article, aside, audio, canvas, caption, details, div, figure, footer, header, hgroup, iframe, img, mark, menu, nav, object, section, span, summary, table, tbody, td, tfoot, thead, tr, video {
    border: 0;
    margin: 0;
    padding: 0;
}
div {
    word-break: break-all;
}
.share-content, .share-download, .share-table {
    height: 100%;
    -webkit-touch-callout: text;
    -webkit-user-select: text;
    -moz-user-select: text;
    -ms-user-select: text;
    user-select: text;
}

.share-content, .share-download, .share-office, .share-pdf, .share-table {
    display: block;
    position: relative;
    width: 100%;
}

#share-notice {
    color: #666666;
    margin: 20px auto;
    width: 400px;
    
}

</style>
