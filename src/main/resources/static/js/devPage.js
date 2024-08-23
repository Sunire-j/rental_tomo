function getNickname_getUserId(){


    document.getElementById("accessTokenDisplay").textContent = "엑세스 토큰이 없습니다.";
    document.getElementById("refreshTokenDisplay").textContent = "리프레시 토큰이 없습니다.";

    if (1) {
        fetch("/api/v1/users/nickname", {
            method: "GET",
            credentials: 'include' // 쿠키를 포함하도록 설정
        })
            .then(response => {
                if (!response.ok) {
                    return response.json()
                        .then(async message=>{
                            // message에 expired가 나오면 리프레시 토큰 보내도록 해야함.
                            if(message.message === 'Access token expired. Please refresh your token.' && getCookie('refreshToken')){
                                var result = await refreshAccessToken();
                                if(!result){
                                    throw new Error("토큰만료 다시 로그인");
                                }else{
                                    console.log("토큰 재발행 성공");
                                    getNickname_getUserId();
                                }
                            }
                        })
                }
                return response.text();
            })
            .then(data => {
                // 유저 아이디 표시
                document.getElementById("userNicknameDisplay").textContent = data || "유저 닉네임을 찾을 수 없습니다.";
            })
            .catch(error => {
                document.getElementById("userNicknameDisplay").textContent = "Error: " + error.message;
            });

        fetch("/api/v1/users/getId", {
            method: "GET",
            credentials: 'include' // 쿠키를 포함하도록 설정
        })
            .then(response => {
                if (!response.ok) {
                    return response.text()
                        .then(async message=>{
                            // message에 expired가 나오면 리프레시 토큰 보내도록 해야함.
                            if(message === 'Access token expired. Please refresh your token.' && getCookie('refreshToken')){
                                var result = await refreshAccessToken();
                                if(!result){
                                    throw new Error("토큰만료 다시 로그인");
                                }else{
                                    console.log("토큰 재발행 성공");
                                    getNickname_getUserId();
                                }
                            }

                        })
                }
                return response.text(); // 응답을 JSON으로 처리
            })
            .then(data => {
                // 유저 아이디 표시
                document.getElementById("userIdDisplay").textContent = data || "유저 아이디를 찾을 수 없습니다.";
            })
            .catch(error => {
                document.getElementById("userIdDisplay").textContent = "Error: " + error.message;
            });

    } else {
        document.getElementById("userNicknameDisplay").textContent = "엑세스 토큰이 없으므로 유저 닉네임을 가져올 수 없습니다.";
    }
}

function postTest(){
    const form = document.getElementById("need_authForm");
    document.getElementById('need_authForm').addEventListener('submit', function(e) {
        e.preventDefault();
        postTest_inner();
        function postTest_inner(){
            const token = getCookie('accessToken'); // 토큰 가져오기
            const formData = new FormData(form); // 폼 데이터 가져오기

            fetch('/api/v1/reviews/write', {
                method: 'POST',
                credentials: 'include', // 쿠키를 포함하도록 설정
                body: formData
            })
                .then(response => {
                    if (!response.ok) {
                        return response.json()
                            .then(async message=>{
                                // message에 expired가 나오면 리프레시 토큰 보내도록 해야함.
                                console.log(message.message);
                                if(message.message === 'Access token expired. Please refresh your token.' && getCookie('refreshToken')){
                                    var result = await refreshAccessToken();
                                    if(!result){
                                        throw new Error("토큰만료 다시 로그인");
                                    }else{
                                        console.log("토큰 재발행 성공");
                                        postTest_inner();
                                    }
                                }

                            })
                    }else{
                        return response.text()
                            .then(async message=>{
                                const message_ = await message;
                                alert(message_);
                            })
                    }
                })
                .catch(error => console.error(error));
        }

    });
}

document.addEventListener("DOMContentLoaded", getNickname_getUserId);
document.addEventListener("DOMContentLoaded", postTest);
