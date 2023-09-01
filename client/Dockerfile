FROM node:lts-alpine3.18
LABEL authors="Michał Leśkiewicz"

WORKDIR ./

# Install dependencies
COPY package*.json .
RUN npm i

# Copy source code
COPY . .

# Build app
RUN npm run build

EXPOSE 3000

ENTRYPOINT ["npx", "serve", "-s", "build"]
