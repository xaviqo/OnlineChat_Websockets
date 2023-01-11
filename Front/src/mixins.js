const SESSION_NAME = 'ffchat_session';
export const mixins = {
    methods: {
        saveSession(data){
            localStorage.setItem(SESSION_NAME, JSON.stringify(data));
        },
        removeSession(){
            localStorage.removeItem(SESSION_NAME);
        },
        isSessionSetted() {
            return (localStorage.getItem(SESSION_NAME) != null)
        },
        getChatId(){
            return JSON.parse(localStorage.getItem(SESSION_NAME)).chatId;
        },
        getSession(){
            return JSON.parse(localStorage.getItem(SESSION_NAME));
        },
        getNickname(){
            return JSON.parse(localStorage.getItem(SESSION_NAME)).userNickname;
        },
        getUserId(){
            return JSON.parse(localStorage.getItem(SESSION_NAME)).userId;
        },
        getAvatarUrl(){
            return JSON.parse(localStorage.getItem(SESSION_NAME)).avatarUrl;
        },
        getAccessToken(){
            return JSON.parse(localStorage.getItem(SESSION_NAME))['tokenPayload'].accessToken;
        },
        getRefreshToken(){
            return JSON.parse(localStorage.getItem(SESSION_NAME))['tokenPayload'].refreshToken;
        },
        avatarizeNickname(nickname,style){
            return `https://avatars.dicebear.com/api/${style}/${nickname}.svg`
        },
        isMyId(id){
            return (id == this.getUserId());
        }
    }
}