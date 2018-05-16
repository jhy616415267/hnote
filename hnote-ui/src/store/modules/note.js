import { getNoteById, getLastestNotes } from '@/api/note'

const note = {
    state: {
        note: '',
        currentSelectedNote: ''
    },
    mutations: {
        SET_NOTE: (state, note) => {
          state.note = note
        },
        SET_CURRENT_SELECTED_NOTE: (state, note) => {
          state.currentSelectedNote = note
        } 
    },
    actions: {
        GetNoteInfoById({ commit }, noteId) {
            return new Promise((resolve, reject) => {
                getNoteById(noteId).then(response => {
                  if (!response.status == 200) { // 由于mockjs 不支持自定义状态码只能这样hack
                    reject('error')
                  } else if (response.status == 200) {
                    commit('SET_NOTE', response.data)
                  } else if (response.status == 204) {
                    commit('SET_NOTE', '')
                  }

                  resolve(response)
                }).catch(error => {
                  reject(error)
                })
            })
        },
        ClearNoteInfo({ commit }) {
          commit('SET_NOTE', '')
        },
        SetCurrentSelectedNote({ commit }, note) {
          commit('SET_CURRENT_SELECTED_NOTE', note)
        }
    }
}

export default note