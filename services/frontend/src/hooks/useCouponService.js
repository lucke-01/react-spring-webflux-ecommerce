import { useContext } from "react";
import axios from "axios";
import {AuthContext} from "context";
import {BASE_API_URL} from "utils";

export const useCouponService = () => {
    const COUPON_API = "coupon";
    const { token } = useContext(AuthContext);
    const config = {
        headers: {
            Authorization: `Bearer ${token}`,
        }
    };
    const getAllCoupons = async () => {
        const url = `${BASE_API_URL}/${COUPON_API}`;
        return await axios.get(url, config).then(data => data.data);
    }
    const getCouponById = async (id) => {
        const url = `${BASE_API_URL}/${COUPON_API}/${id}`;
        return await axios.get(url, config).then(data => data.data);
    };
    const createCoupon = async (coupon) => {
        const url = `${BASE_API_URL}/${COUPON_API}`;
        return await axios.post(url, coupon, config).then(data => data.data);
    };
    const updateCoupon = async (coupon) => {
        const url = `${BASE_API_URL}/${COUPON_API}`;
        return await axios.patch(url, coupon, config).then(data => data.data);
    };
    const removeCouponById = async (id) => {
        const url = `${BASE_API_URL}/${COUPON_API}/${id}`;
        return await axios.delete(url, config).then(data => data.data);
    };
    
    return {
        getAllCoupons,
        getCouponById,
        createCoupon,
        updateCoupon,
        removeCouponById
    };
}