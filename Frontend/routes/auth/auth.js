const express = require('express');
const router = express.Router();
const path = require('path');

router.get('/unauthorized', (req, res) => {
    res.sendFile(path.join(__dirname, '../../public/html/auth/unauthorized.html'))
})
router.get('/register', (req, res) => {
    res.sendFile(path.join(__dirname, '../../public/html/auth/register.html'))
})

module.exports = router;