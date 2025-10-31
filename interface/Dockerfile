FROM node:lts-alpine AS build

ENV API_BASE_URL=""
ENV SW=true

WORKDIR /app

COPY . .

RUN npm install

RUN npm run build

FROM node:lts-alpine

COPY --from=build /app/.output /app/.output

ENV PORT=80
ENV HOST=0.0.0.0

EXPOSE 80

CMD ["node", "app/.output/server/index.mjs"]
