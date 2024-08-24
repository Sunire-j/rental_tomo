function editUserInfo() {

    if (document.getElementById("edit_form")) {
        document.getElementById("edit_form").addEventListener("submit", async function (event) {
            event.preventDefault();

            const formData = new FormData(this);
            const jsonObject = {"id": null};

            formData.forEach((value, key) => {
                jsonObject[key] = value;
            });

            const jsonString = JSON.stringify(jsonObject);

            const response = await fetch("/api/v1/users/edit", {
                method: "POST",
                credentials: 'include',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: jsonString
            });

            if (!response.ok) {
                // 발생할 수 있는 예외는 닉네임중복
                const errorMessage = await response.text();
                const [errorCode, message] = errorMessage.split('/');

                let formattedMessage = '';

                if (errorCode === 'NICKNAME_DUPLICATED') {
                    formattedMessage = `가입 실패(닉네임 중복) : ${message}`;
                    document.querySelector('input[name="nickname"]').focus(); // 닉네임 입력 폼으로 포커스 이동
                } else {
                    formattedMessage = '알 수 없는 오류 발생';
                }
                alert(formattedMessage);

            } else {
                alert("수정 완료");
                location.reload();
            }
        });
    }
}


function editUserPwd() {
    if (document.getElementById("pwd_form")) {

        document.getElementById("pwd_form").addEventListener("submit", async function (event) {
            event.preventDefault();

            const jsonObject = {"id": null};

            var pass = document.getElementById("password").value;
            var elementById = document.getElementById("passwordre").value;

            if (pass !== elementById) {
                alert("비밀번호가 다릅니다.");
                form.password.focus();
                return;
            }

            jsonObject["password"] = pass;

            const jsonString = JSON.stringify(jsonObject);

            const response = await fetch("/api/v1/users/edit", {
                method: "POST",
                credentials: 'include',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: jsonString
            });

            if (!response.ok) {
                alert("알 수 없는 오류 발생");

            } else {
                alert("수정 완료. 다시 로그인해주세요.");
                location.href("/api/v1/users/logout");
            }
        });
    }
}

function checkConditions() {
    if(document.getElementById("allowCheck")) {


        const allowCheck = document.getElementById('allowCheck').checked;
        const nicknameInput = document.getElementById('nickname');
        const finalCheck = document.getElementById('finalCheck').value;

        // placeholder와 nickname의 value 비교
        const placeholderValue = nicknameInput.placeholder;
        const isNicknameValid = nicknameInput.value === placeholderValue;

        // 조건 확인
        const isButtonEnabled = allowCheck && isNicknameValid && finalCheck == 100;

        // 버튼 활성화/비활성화
        document.getElementById('deleteId').disabled = !isButtonEnabled;
    }
}

function move_to_deleteId(){
    if(confirm("확인 버튼을 선택하면, 탈퇴는 되돌릴 수 없습니다. 정말 진행하시겠습니까?")){
        location.href="/mypage/deleteid2";
    }
}

document.addEventListener("DOMContentLoaded", editUserInfo);
document.addEventListener("DOMContentLoaded", editUserPwd);
document.addEventListener("DOMContentLoaded", checkConditions);
