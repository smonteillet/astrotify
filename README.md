Astrotify is a project that notifies you each morning when evening will be worth getting out your telescope for astronomical observation.

# Compile

To compile and run test, use maven `mvn clean package`

# Architecture

Astrotify use [Meteoblue](https://content.meteoblue.com/en/spatial-dimensions/air/astronomy-seeing) to gather astronomical data near your location.

Astrotify use Google Cloud Platform and is deployed as a Cloud Function. It is triggered by topic subscription, sent by a Cloud Scheduler on a daily basis.

Currently, Astrotify will notify you on Telegram using [Astrotify Telegram Bot](http://t.me/AstrotifyBot)

# Deploy

You will need the following env vars : 
 - `METEOBLUE_URL` : Meteoblue URL for your location
 - `TELEGRAM_CHAT_ID` : Telegram chat id where you will be notify (look at this [tutorial](https://rieckpil.de/howto-send-telegram-bot-notifications-with-java/) in order to get chat Id)

Astrotify also use Google Secret Manager to store/fetch Astrotify Telegram Bot API Token. Secret ID is `ASTROTIFY_TELEGRAM_BOT_TOKEN`

To deploy astrotify to GCP, use `gcp-function-deploy.sh`
