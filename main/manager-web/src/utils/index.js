import { Message } from 'element-ui'
import router from '../router'
import Constant from '../utils/constant'


export function checkUserLogin(fn) {
    let token = localStorage.getItem(Constant.STORAGE_KEY.TOKEN)
    let userType = localStorage.getItem(Constant.STORAGE_KEY.USER_TYPE)
    if (isNull(token) || isNull(userType)) {
        goToPage('console', true)
        return
    }
    if (fn) {
        fn()
    }
}


export function isNull(data) {
    if (data === undefined) {
        return true
    } else if (data === null) {
        return true
    } else if (typeof data === 'string' && (data.length === 0 || data === '' || data === 'undefined' || data === 'null')) {
        return true
    } else if ((data instanceof Array) && data.length === 0) {
        return true
    }
    return false
}


export function isNotNull(data) {
    return !isNull(data)
}


export function showDanger(msg) {
    if (isNull(msg)) {
        return
    }
    Message({
        message: msg,
        type: 'error',
        showClose: true
    })
}


export function showWarning(msg) {
    if (isNull(msg)) {
        return
    }
    Message({
        message: msg,
        type: 'warning',
        showClose: true
    });
}




export function showSuccess(msg) {
    Message({
        message: msg,
        type: 'success',
        showClose: true
    })
}




export function goToPage(path, isRepalce) {
    if (isRepalce) {
        router.replace(path)
    } else {
        router.push(path)
    }
}


export function getCurrentPage() {
    let hash = location.hash.replace('#', '')
    if (hash.indexOf('?') > 0) {
        hash = hash.substring(0, hash.indexOf('?'))
    }
    return hash
}


export function randomNum(min, max) {
    return Math.round(Math.random() * (max - min) + min)
}



export function getUUID() {
    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, c => {
        return (c === 'x' ? (Math.random() * 16 | 0) : ('r&0x3' | '0x8')).toString(16)
    })
}



export function validateMobile(mobile, areaCode) {

    const cleanMobile = mobile.replace(/\D/g, '');


    switch (areaCode) {
        case '+86':
            return /^1[3-9]\d{9}$/.test(cleanMobile);
        case '+852':
            return /^[569]\d{7}$/.test(cleanMobile);
        case '+853':
            return /^6\d{7}$/.test(cleanMobile);
        case '+886':
            return /^9\d{8}$/.test(cleanMobile);
        case '+1':
            return /^[2-9]\d{9}$/.test(cleanMobile);
        case '+44':
            return /^7[1-9]\d{8}$/.test(cleanMobile);
        case '+81':
            return /^[7890]\d{8}$/.test(cleanMobile);
        case '+82':
            return /^1[0-9]\d{7}$/.test(cleanMobile);
        case '+65':
            return /^[89]\d{7}$/.test(cleanMobile);
        case '+61':
            return /^[4578]\d{8}$/.test(cleanMobile);
        case '+49':
            return /^1[5-7]\d{8}$/.test(cleanMobile);
        case '+33':
            return /^[67]\d{8}$/.test(cleanMobile);
        case '+39':
            return /^3[0-9]\d{8}$/.test(cleanMobile);
        case '+34':
            return /^[6-9]\d{8}$/.test(cleanMobile);
        case '+55':
            return /^[1-9]\d{10}$/.test(cleanMobile);
        case '+91':
            return /^[6-9]\d{9}$/.test(cleanMobile);
        case '+971':
            return /^[5]\d{8}$/.test(cleanMobile);
        case '+966':
            return /^[5]\d{8}$/.test(cleanMobile);
        case '+880':
            return /^1[3-9]\d{8}$/.test(cleanMobile);
        case '+234':
            return /^[789]\d{9}$/.test(cleanMobile);
        case '+254':
            return /^[17]\d{8}$/.test(cleanMobile);
        case '+255':
            return /^[67]\d{8}$/.test(cleanMobile);
        case '+7':
            return /^[67]\d{9}$/.test(cleanMobile);
        default:

            return /^\d{5,15}$/.test(cleanMobile);
    }
}



export function generateSm2KeyPairHex() {

    const sm2 = require('sm-crypto').sm2;
    const keypair = sm2.generateKeyPairHex();
    
    return {
        publicKey: keypair.publicKey,
        privateKey: keypair.privateKey,
        clientPublicKey: keypair.publicKey,
        clientPrivateKey: keypair.privateKey
    };
}


export function sm2Encrypt(publicKey, plainText) {
    if (!publicKey) {
        throw new Error('nullundefined');
    }
    
    if (!plainText) {
        throw new Error('');
    }
    
    const sm2 = require('sm-crypto').sm2;

    const encrypted = sm2.doEncrypt(plainText, publicKey, 1);

    const result = "04" + encrypted;
    
    return result;
}


export function sm2Decrypt(privateKey, cipherText) {
    const sm2 = require('sm-crypto').sm2;

    const dataWithoutPrefix = cipherText.startsWith("04") ? cipherText.substring(2) : cipherText;

    return sm2.doDecrypt(dataWithoutPrefix, privateKey, 1);
}


export function debounce(fn, delay = 500, immediate = false) {
    let timer = null;
    
    return function (...args) {
        const context = this;
        
        if (timer) {
            clearTimeout(timer);
        }
        
        if (immediate && !timer) {
            fn.apply(context, args);
        }
        
        timer = setTimeout(() => {
            if (!immediate) {
                fn.apply(context, args);
            }
            timer = null;
        }, delay);
    };
}

