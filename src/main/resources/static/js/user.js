
//로그인 Post요청 처리
function login(){
    const loginform = document.querySelector("#loginForm");

    if (loginform) {
        document.getElementById("loginForm").onsubmit = function (event) {
            event.preventDefault(); // 기본 폼 제출 방지
            const formData = new FormData(this);
            const jsonData = {};

            // FormData를 JSON으로 변환
            formData.forEach((value, key) => {
                jsonData[key] = value;
            });

            fetch("/api/v1/users/login", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(jsonData)
            })
                .then(response => {
                    if (!response.ok) {
                        throw new Error("로그인 실패");
                    }
                    return response.json(); // 응답을 JSON으로 처리
                })
                .then(tokens => {
                    setCookie("accessToken","Bearer " + tokens.accessToken);
                    setCookie("refreshToken", "Bearer " + tokens.refreshToken);
                    alert("로그인 성공: " + tokens.accessToken);
                    location.reload(); // 페이지 새로 고침
                })
                .catch(error => {
                    alert("로그인 실패, 사유 :" + error.message);
                });
        };
    }
}

//회원가입 시 약관동의 체크처리
function join_check_terms(){
    const terms_select = document.querySelector("#all_terms");
    if(terms_select){
        const allTermsCheckbox = document.querySelector("#all_terms");
        const terms1Checkbox = document.querySelector("#terms1");
        const terms2Checkbox = document.querySelector("#terms2");
        const nextButton = document.querySelector(".grid2_button");

        // "모두 동의합니다" 체크박스 클릭 이벤트
        allTermsCheckbox.addEventListener("change", function () {
            const isChecked = this.checked;
            terms1Checkbox.checked = isChecked;
            terms2Checkbox.checked = isChecked;
            updateNextButtonState();
        });

        // 개별 약관 체크박스 클릭 이벤트
        terms1Checkbox.addEventListener("change", updateNextButtonState);
        terms2Checkbox.addEventListener("change", updateNextButtonState);

        function updateNextButtonState() {
            const terms1Checked = terms1Checkbox.checked;
            const terms2Checked = terms2Checkbox.checked;

            // "모두 동의합니다" 체크박스 상태 자동 조정
            allTermsCheckbox.checked = terms1Checked && terms2Checked;

            // "다음" 버튼 활성화/비활성화
            if (terms1Checked && terms2Checked) {
                nextButton.disabled = false;
            } else {
                nextButton.disabled = true;
            }
        }

        nextButton.addEventListener("click", function () {
            location.href = "/join/2";
        })
    }
}


//회원가입 시 정보입력 후 post요청
function join_insertUserInfo(){
    const join_form = document.querySelector("#join_form");
    if(join_form){
        document.getElementById("join_form").addEventListener("submit", async function (event) {
            event.preventDefault(); // 기본 submit 이벤트 방지

            const form = event.target;
            const password = form.password.value;
            const passwordre = form.passwordre.value;
            const phonenum = form.phonenum.value;

            // 1. 비밀번호 확인
            if (password !== passwordre) {
                alert("비밀번호가 일치하지 않습니다.");
                form.password.focus(); // 비밀번호 입력 폼으로 포커스 이동
                return;
            }

            // 2. 휴대폰 번호 유효성 검사
            const phonenumPattern = /^010\d{8}$/;
            if (!phonenumPattern.test(phonenum)) {
                alert("휴대폰 번호는 010으로 시작하는 11자리 숫자여야 합니다.");
                form.phonenum.focus(); // 휴대폰 번호 입력 폼으로 포커스 이동
                return;
            }

            // 3. POST 요청 보내기
            const userJoinRequest = {
                userid: form.userid.value,
                password: password,
                email: form.email.value,
                phonenum: phonenum,
                birth: form.birth.value,
                sex: form.sex.value,
                nickname: form.nickname.value
            };

            try {
                const response = await fetch('/api/v1/users/join', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify(userJoinRequest),
                });

                // 4. 응답 처리
                if (!response.ok) {
                    const errorMessage = await response.text();
                    const [errorCode, message] = errorMessage.split('/');

                    console.log(errorCode);
                    console.log("####");
                    console.log(message);

                    let formattedMessage = '';

                    if (errorCode === 'USERNAME_DUPLICATED') {
                        formattedMessage = `가입 실패(아이디 중복) : ${message}`;
                        document.querySelector('input[name="userid"]').focus(); // 아이디 입력 폼으로 포커스 이동
                    } else if (errorCode === 'NICKNAME_DUPLICATED') {
                        formattedMessage = `가입 실패(닉네임 중복) : ${message}`;
                        document.querySelector('input[name="nickname"]').focus(); // 닉네임 입력 폼으로 포커스 이동
                    } else {
                        formattedMessage = `가입 실패 : ${message}`;
                    }

                    alert(formattedMessage);
                } else {
                    alert("가입 성공! 로그인페이지로 이동합니다.");
                    location.href = "/login";
                }
            } catch (error) {
                console.error(`오류 발생: ${error.message}`);
            }
        });
    }
}


document.addEventListener("DOMContentLoaded", login);
document.addEventListener("DOMContentLoaded", join_check_terms);
document.addEventListener("DOMContentLoaded", join_insertUserInfo);