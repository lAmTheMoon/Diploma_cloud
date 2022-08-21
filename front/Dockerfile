FROM node:14.5.0-alpine
RUN mkdir -p /app/front
WORKDIR /app/front
COPY package*.json ./
RUN npm install
COPY . .
EXPOSE 8080
CMD [ "npm", "run", "serve" ]