import { normalizeDateAndTime, normalizeDateToUTC } from "./general.utils";

export const normalizeCoupon = ({coupon, superAdmin, onEdit}) => {
    return {
        ...coupon,
        activeLabel: coupon?.active ? "Yes" : "No",
        activeFrom: normalizeDateAndTime(coupon.activeFrom),
        activeTo: normalizeDateAndTime(coupon.activeTo),
        idTable: coupon.id
    }
}
export const normalizeCoupons = ({coupons , superAdmin, onEdit}) => {
    return coupons.map(coupon => 
        normalizeCoupon({coupon: coupon , superAdmin, onEdit})
    );
}
export const DISCOUNT_TYPES = [
    { value: "PERCENT", label: "Percent" },
    { value: "AMOUNT", label: "Amount" }
  ];
export const DEFAULT_COUPON = {
    name: "",
    code: "",
    activeFrom: normalizeDateToUTC(new Date()),
    activeTo: normalizeDateToUTC(new Date()),
    discount: 0,
    discountType: "AMOUNT",
    dayOfWeek: {
        sunday: false,
          monday: true,
          tuesday: true,
          wednesday: true,
          thursday: true,
          friday: true,
          saturday: false
    },
    shortDescription: "",
    active: true,
    locationProducts: []
}