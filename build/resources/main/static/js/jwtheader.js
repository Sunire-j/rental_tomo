document.addEventListener("DOMContentLoaded", function() {
    const accessToken = getCookie("accessToken");
    const refreshToken = getCookie("refreshToken");
    if (accessToken) {
        const metaToken = document.createElement('meta');
        metaToken.name = "Authorization";
        metaToken.content = accessToken; // Bearer는 이미 accessToken에 포함되어 있음
        document.head.appendChild(metaToken);
    }
});

async function refreshAccessToken() {
    // const refreshToken = localStorage.getItem('refreshToken');
    const refreshToken = getCookie('refreshToken');

    if (!refreshToken) {
        console.error("리프레시 토큰이 없습니다.");
        return false; // 리프레시 토큰이 없으면 false 반환
    }

    try {
        // 리프레시 토큰을 사용하여 새로운 엑세스 토큰을 요청
        const response = await fetch("/api/v1/token/refresh", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "refresh_token": refreshToken
            }
        });

        if (!response.ok) {
            throw new Error('리프레시 토큰이 유효하지 않거나 서버 오류');
        }

        const data = await response.json();

        // 새로운 엑세스 토큰을 로컬 스토리지에 저장
        // localStorage.setItem('accessToken', "Bearer " + data.accessToken);
        setCookie('accessToken', "Bearer " + data.accessToken);

        // 필요시 리프레시 토큰도 업데이트
        if (data.refreshToken) {
            // localStorage.setItem('refreshToken', "Bearer " + data.refreshToken);
            setCookie('refreshToken', "Bearer " + data.refreshToken);
        }

        return true; // 성공적으로 갱신된 경우 true 반환
    } catch (error) {
        console.error("엑세스 토큰 갱신 실패:", error);
        return false; // 오류 발생 시 false 반환
    }
}
