function changeSellerStatus() {
    if (document.getElementById("set-seller")) {

        const checkbox = document.getElementById("set-seller");
        checkbox.addEventListener('change', () => {
            let isChecked = checkbox.checked;
            //isChecked가 트루면

            fetch('/api/v1/seller/onoff', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({status: isChecked}) // status를 JSON 형식으로 전송
            })
                .then(response => response.text())
                .then(data => {
                    console.log('서버 응답:', data);
                    location.reload();
                })
                .catch(error => {
                    console.error('오류 발생:', error);
                    location.reload();
                });
        });
    }
}

function changeItemAttr() {
    if (document.getElementsByClassName("checkboxs")) {
        document.getElementById("apply").addEventListener("click", () => {
            const data = {};
            const checkboxes = document.querySelectorAll(".item-checkbox");
            const texts = document.querySelectorAll(".item-text");

            checkboxes.forEach((checkbox, index) => {
                const id = checkbox.name;
                data[id] = {
                    checked: checkbox.checked,
                    text: texts[index].value
                };
            });

            fetch('/api/v1/seller/itemattrchange', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(data)
            })
                .then(response => response.json())
                .then(responseData => {
                    console.log('서버 응답:', responseData);
                    // 추가적인 로직 (예: 성공 메시지 표시)
                })
                .catch(error => {
                    console.error('오류 발생:', error);
                });
        });
    }
}

function sellerItemOnOff(checkbox) {
    if (document.getElementsByClassName("checkboxs")) {
        const inputName = checkbox.name + '_smy'; // children_.id + '_smy'
        const inputField = document.querySelector(`input[name='${inputName}']`);
        if (inputField) {
            inputField.disabled = !checkbox.checked;
            if (!checkbox.checked) {
            }
        }
    }
}

document.addEventListener("DOMContentLoaded", changeSellerStatus);
document.addEventListener("DOMContentLoaded", changeItemAttr);
document.addEventListener("DOMContentLoaded", sellerItemOnOff);