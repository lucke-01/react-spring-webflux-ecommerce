import dayjs from "dayjs";
const utc = require("dayjs/plugin/utc");
dayjs.extend(utc);

/**
 * BASE API URL: used to know server's url, firstly find by env and if not set there find in windows.apiBaseUrl
 */
export const BASE_API_URL =
  process.env.REACT_APP_API_BASE_URL || window.apiBaseUrl;

export const USER_LIST_COLUMNS = [
  { key: "login", label: "Username" },
  { key: "email", label: "Email" },
  { key: "lastLogin", label: "Last login date" },
  { key: "rolePretty", label: "Role" },
  { key: "actions", label: "Actions" },
];
export const COUPONS_LIST_COLUMNS = [
  { key: "name", label: "Name" },
  { key: "code", label: "Code" },
  { key: "activeFrom", label: "Active to" },
  { key: "activeTo", label: "Active from" },
  { key: "activeLabel", label: "Active" },
  { key: "actions", label: "Actions" }
];
export const DEFAULT_COUPON_FILTER = {
  textSearch: "",
  active: true
};

export const WEEK_DAYS_CHECKBOX = [
  {
    key: "Sunday",
    value: "sunday",
  },
  {
    key: "Monday",
    value: "monday",
  },
  {
    key: "Tuesday",
    value: "tuesday",
  },
  {
    key: "Wednesday",
    value: "wednesday",
  },
  {
    key: "Thursday",
    value: "thursday",
  },
  {
    key: "Friday",
    value: "friday",
  },
  {
    key: "Saturday",
    value: "saturday",
  },
];

export const normalizeDateAndTime = (date) => {
  return dayjs(date).format("DD/MM/YYYY HH:mm");
};

export const normalizeDateToUTC = (date) => {
  if (date == null || date === "") {
    return null;
  }
  return dayjs(date).format("YYYY-MM-DD HH:mm:00");
};

export const getBase64 = (file) => {
  return new Promise((resolve) => {
    const reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onload = () => resolve(reader.result);
  });
};

export const errorControl = (error) => {
  if (typeof error === "string") {
    return error;
  } else if (typeof error === "object" && error.error) {
    return error.error;
  } else {
    return "Ups! there've been an error, please contact with the administrator.";
  }
};

export const screens = {
  users: "users",
  coupons: "coupons"
};

export const roles = {
  admin: "ADMIN",
  adminRead: "ADMIN_READ_ONLY",
  management: "MANAGEMENT"
};

export const userActions = {
  create: "create",
  edit: "edit",
  delete: "delete",
  view: "view",
};

export const allowActionBaseOnRole = (screen, role, action) => {
  const permissions = {
    ADMIN: {
      users: ["create", "edit", "delete", "view"],
      coupons: ["create", "edit", "delete", "view"]
    },
    ADMIN_READ_ONLY: {
      users: null,
      coupons: ["view"]
    },
    MANAGEMENT: {
      users: null,
      coupons: ["create", "edit", "view"]
    }
  };
  try {
    return permissions[role][screen].includes(action);
  } catch (error) {
    return false;
  }
};
