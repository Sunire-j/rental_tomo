document.addEventListener("DOMContentLoaded", function() {
    const accessToken = localStorage.getItem("accessToken");
    const refreshToken = localStorage.getItem("refreshToken");
    if (accessToken) {
        const metaToken = document.createElement('meta');
        metaToken.name = "Authorization";
        metaToken.content = accessToken; // Bearer는 이미 accessToken에 포함되어 있음
        document.head.appendChild(metaToken);
    }
});

async function refreshAccessToken() {
    const refreshToken = localStorage.getItem('refreshToken');

    if (!refreshToken) {
        console.error("리프레시 토큰이 없습니다.");
    }

    // 리프레시 토큰을 사용하여 새로운 엑세스 토큰을 요청
    fetch("/api/v1/token/refresh", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "refresh_token" : refreshToken
        }
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('리프레시 토큰이 유효하지 않거나 서버 오류');
                return false;
            }
            return response.json();
        })
        .then(data => {
            // 새로운 엑세스 토큰을 로컬 스토리지에 저장
            localStorage.setItem('accessToken', data.accessToken);
            // 필요시 리프레시 토큰도 업데이트
            if (data.refreshToken) {
                localStorage.setItem('refreshToken', data.refreshToken);
            }
            return true;
        })
        .catch(error => {
            console.error("엑세스 토큰 갱신 실패:", error);
            return false;
        });
}