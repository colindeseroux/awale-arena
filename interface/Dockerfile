FROM node:lts-alpine AS build

WORKDIR /app

COPY . .

RUN npm install

RUN npm run build

FROM node:lts-alpine

WORKDIR /app

COPY --from=build /app/.output /app/.output

RUN mkdir -p /app/data && \
    chmod 777 /app/data

ENV PORT=80
ENV HOST=0.0.0.0

EXPOSE 80

CMD ["node", "/app/.output/server/index.mjs"]
