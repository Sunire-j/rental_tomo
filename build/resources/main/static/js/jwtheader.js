document.addEventListener("DOMContentLoaded", function() {
    const token = localStorage.getItem("authToken");
    if (token) {
        const metaToken = document.createElement('meta');
        metaToken.name = "Authorization";
        metaToken.content = "Bearer " + token;
        document.head.appendChild(metaToken);
    }
});