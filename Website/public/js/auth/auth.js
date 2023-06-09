import {baseUrl, userApiUrl} from "../shared.js";


export const isAuthenticatedWithRedirect = async () => {
    if (await isAuthenticated() === false) {
        loginWithRedirect();
    }
}

export const isAuthenticated = async () => {
    if (await verifyJwt() === 200) {
        return true;
    }
    return await useRefreshToken() === 200;

}

export const loginWithRedirect = () => {
    window.location.assign(`${baseUrl}?redirect=${window.location.pathname}${window.location.search}`);
}
const verifyJwt = async () => {
    let status = 500;
    let jwt = window.localStorage.getItem("jwt");
    if (!jwt) {
        return 403;
    }
    const url = `${userApiUrl}/api/auth/validate`;

    if (jwt.startsWith('Bearer ')) {
        jwt = jwt.substring(7);
    }

    const data = {token: jwt};
    await fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
        .then(response => {
            status = response.status;
        })
        .catch(error => {
            console.error('There was an error: ' + error);
        })
    return status;
}
const useRefreshToken = async () => {
    let refreshToken = window.localStorage.getItem("refreshToken");
    if (typeof refreshToken !== 'string' || refreshToken.trim().length === 0) {
        console.error('No refresh token found');
        return 403;
    }

    const url = `${userApiUrl}/api/auth/refresh`;
    const data = {refreshToken: refreshToken};
    const response = await fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    });
    if (response.status === 200) {
        const body = await response.json();
        window.localStorage.setItem('refreshToken', body.refreshToken);
        window.localStorage.setItem('jwt', body.accessToken);
        return 200;
    } else {
        console.error('Refresh token is not valid');
        return 403;
    }
}
export const getJwtPayload = async () => {
    if (window.localStorage.getItem("jwt") === null) {
        throw Error('No jwt');
    }

    const base64Url = window.localStorage.getItem("jwt").split('.')[1];
    const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
    const jsonPayload = decodeURIComponent(window.atob(base64).split('').map(function (c) {
        return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
    }).join(''));

    return JSON.parse(jsonPayload);
}

export const logoutUser = () => {
    window.localStorage.removeItem('jwt');
    window.localStorage.removeItem('refreshToken');
    window.location.assign(`${baseUrl}/`);
}

export const isAdmin = async () => {
    const payload = await getJwtPayload();
    const role = payload['role'];
    return role.some(role => role['name'] === 'ADMIN')
}
export const isUser = async () => {
    const payload = await getJwtPayload();
    const role = payload['role'];
    return role.some(role => role['name'] === 'USER')
}