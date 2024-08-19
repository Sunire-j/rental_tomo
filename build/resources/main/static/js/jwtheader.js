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