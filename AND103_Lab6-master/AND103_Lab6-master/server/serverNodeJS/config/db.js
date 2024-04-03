const mongoose = require('mongoose');

const local = "mongodb+srv://nta2004:oehVfrn9Cemw5rgn@cluster0.xopjfar.mongodb.net/ph36936";

const connect = async () => {
    try {
        await mongoose.connect(local);
        console.log('Connect success');
    } catch (error) {
        console.error('Connection to MongoDB failed:', error);
    }
}

module.exports = { connect };
