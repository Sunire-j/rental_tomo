function getCookie(name){
    const value = `; ${document.cookie}`;
    const parts = value.split(`; ${name}=`);
    if (parts.length === 2) return parts.pop().split(';').shift();
}

function setCookie(name, value){
    const expires = new Date(Date.now() + 7 * 864e5).toUTCString();
    document.cookie = `${name}=${value}; expires=${expires}; path=/;`;
}

