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
        getSession(){
            return JSON.parse(localStorage.getItem(SESSION_NAME));
        },
        getChatId(){
            return JSON.parse(localStorage.getItem(SESSION_NAME)).id;
        },
        getNickname(){
            return JSON.parse(localStorage.getItem(SESSION_NAME)).userNickname;
        },
        getUserId(){
            return JSON.parse(localStorage.getItem(SESSION_NAME)).userId;
        },
        getRoomTopic(){
            return JSON.parse(localStorage.getItem(SESSION_NAME)).roomTopic;
        },
        getRoomPassword(){
            return JSON.parse(localStorage.getItem(SESSION_NAME)).password;
        },
        getAvatarUrl(){
            return JSON.parse(localStorage.getItem(SESSION_NAME)).avatarUrl;
        },
        getAccessToken(){
            return JSON.parse(localStorage.getItem(SESSION_NAME))['tokenPayload'].accessToken;
        },
        getRefreshToken(){
            return JSON.parse(localStorage.getItem(SESSION_NAME))['tokenPayload'].refreshToken;
        }
    }
}