# pull official base image
FROM node:16 AS build

# set working directory
WORKDIR /app
# add `/app/node_modules/.bin` to $PATH
ENV PATH /app/node_modules/.bin:$PATH

# install app dependencies
COPY package.json ./
COPY package-lock.json ./
COPY ./ .
RUN npm install --silent --only=prod
RUN npm install react-scripts@3.4.1 -g --silent

# Building app
RUN npm run build

#--

FROM nginx
COPY --from=build /app/build /usr/share/nginx/html
COPY nginx/nginx.conf /etc/nginx/conf.d/default.conf
CMD ["nginx", "-g", "daemon off;"]