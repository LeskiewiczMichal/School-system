FROM node:lts-alpine3.18
LABEL authors="Michał Leśkiewicz"

ENV NODE_ENV development

WORKDIR ./

COPY package*.json .
RUN npm i

COPY . .

EXPOSE 3000

ENTRYPOINT ["npm", "start"]