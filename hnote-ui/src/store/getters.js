const getters = {
  token: state => state.user.token,
  avatar: state => state.user.avatar,
  name: state => state.user.name,
  errorLogs: state => state.errorLog.logs
}
export default getters
