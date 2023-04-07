/* eslint-disable testing-library/no-node-access */
/* eslint-disable testing-library/no-container */
import { render, screen, cleanup, waitFor } from "@testing-library/react";
import userEvent from '@testing-library/user-event';
import CouponForm from "./CouponForm";
import { rest} from 'msw';
import { setupServer } from 'msw/node';
import { MOCK_COUPONS_LIST } from "mocks";
import {render as renderWithContext} from "tests/test";
import { handlers } from "mocks/handlers";

const couponsPath = '/api/coupon';
// declare which API requests to mock
const server = setupServer(
    ...handlers,
    // capture POST requests
    rest.get(couponsPath, (req, res, ctx) => {
        // respond using a mocked JSON body
        return res(ctx.json(MOCK_COUPONS_LIST));
    }),
    rest.get(couponsPath + "/*", (req, res, ctx) => {
        // respond using a mocked JSON body
        return res(ctx.json(MOCK_COUPONS_LIST[0]));
    })    
);

//MOCKS
//END MOCKS
describe("Coupon form tests",  () => {
    // establish API mocking before all tests
    beforeAll(() => server.listen());
    beforeEach(() => {cleanup();});
    // reset any request handlers that are declared as a part of our tests
    afterEach(() => server.resetHandlers());
    // clean up once the tests are done
    afterAll(() => server.close());

    test("should create form", async () => {
        //GIVEN
        const onSubmitObj = {
            onSumit: () => new Promise((resolve, reject) => {return resolve({})})
        }
        let spyOnSubmit = jest.spyOn(onSubmitObj, "onSumit");
        const userActions = userEvent.setup();
        const sourceCoupon = null;

        const { container } = render(
            <CouponForm sourceCoupon={sourceCoupon} onSubmit={onSubmitObj.onSumit}></CouponForm>
        );
        const saveButton = screen.getByRole('button', {
            name: /save/i
        });

        await userActions.type(container.querySelector(`input[name="name"]`), 'name');
        await userActions.type(container.querySelector(`input[name="code"]`), 'code');
        await userActions.type(container.querySelector(`input[name="discount"]`), '1');
        await userActions.type(container.querySelector(`input[name="shortDescription"]`), 'short description');
        
        //WHEN send form
        await userActions.click(saveButton);
        //THEN
        expect(spyOnSubmit).toBeCalled();
    });
    test("should not create form error", async () => {
        //GIVEN
        const onSubmitObj = {
            onSumit: () => new Promise((resolve, reject) => {return resolve({})})
        }
        let spyOnSubmit = jest.spyOn(onSubmitObj, "onSumit");
        const userActions = userEvent.setup();
        const sourceCoupon = null;

        const { container } = render(
            <CouponForm sourceCoupon={sourceCoupon} onSubmit={onSubmitObj.onSumit}></CouponForm>
        );
        const saveButton = screen.getByRole('button', {
            name: /save/i
        });

        await userActions.type(container.querySelector(`input[name="name"]`), 'name');
        await userActions.type(container.querySelector(`input[name="discount"]`), '-1');
        await userActions.type(container.querySelector(`input[name="shortDescription"]`), 'short description');
        
        //WHEN send form
        await userActions.click(saveButton);
        //THEN
        expect(spyOnSubmit).not.toBeCalled();
    });
    test("should edit form", async () => {
        //GIVEN
        const onSubmitObj = { onSumit: () => new Promise((resolve, reject) => {return resolve({})})}
        let spyOnSubmit = jest.spyOn(onSubmitObj, "onSumit");
        const userActions = userEvent.setup();
        const sourceCoupon = MOCK_COUPONS_LIST[0];

        const { container } = render(
            <CouponForm sourceCoupon={sourceCoupon} onSubmit={onSubmitObj.onSumit}></CouponForm>
        );
        const saveButton = screen.getByRole('button', {
            name: /save/i
        });

        await userActions.type(container.querySelector(`input[name="name"]`), 'nameModified');
        
        //WHEN send form
        await userActions.click(saveButton);
        //THEN
        expect(spyOnSubmit).toBeCalled();
    });
});