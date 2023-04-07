import { rest } from "msw";
import { BASE_API_URL } from "utils";
import { TEST_USER_LIST } from "mocks";

export const handlers = [
  rest.get(`${BASE_API_URL}/administrator`, (req, res, ctx) => {
    return res(ctx.json(TEST_USER_LIST));
  }),
  rest.get(`${BASE_API_URL}/organisation`, (req, res, ctx) => {
    return res(ctx.json([]));
  }),
  rest.get(`/api/location`, (req, res, ctx) => {
    return res(ctx.json([]));
  }),
];
