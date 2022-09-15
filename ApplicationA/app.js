const { Kafka } = require('kafkajs')
var express = require('express')
var cors = require('cors')
var app = express()

const kafka = new Kafka({
  clientId: 'producer-client',
  brokers: ['kafka:9093'],
})

const producer = kafka.producer({
    allowAutoTopicCreation: true
})

const consumer = kafka.consumer({ groupId: 'AppAGroup' })


// Listen for requests
async function sendMessage(topic, message) {
    await producer.connect()
    await producer.send({
    topic: topic,
    messages: [
        { value: message },
    ],
    }).catch(e => console.error(`Error: ${e.message}`, e))

    await producer.disconnect();
}



app.get('/', cors(), async (req, res) => {
    let topic = req.query.topic;
    let message = req.query.message;
    console.log('SendingMessage:')
    console.log('Topic:' + topic)
    await sendMessage(topic, message);
    res.json({ message: 'Message Sent' })
})
  
app.listen(80, () => {
    console.log(`Start listening on port 80`)
})

// Run Consumer

const topic = 'ApplicaitionBMessage'
const run = async () => {
    console.log('Consumer Running')  
    await consumer.connect()
    await consumer.subscribe({ topic, fromBeginning: true })
    await consumer.run({
      eachMessage: async ({ topic, partition, message }) => {
        console.log('Response Received')  
        console.log(`Topic: ${topic},  Value: ${message.value}`)
      },
    })
  }
  
  run().catch(e => console.error(`AppA consumer error: ${e.message}`, e))