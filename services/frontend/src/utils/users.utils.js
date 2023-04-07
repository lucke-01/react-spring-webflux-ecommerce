import { normalizeDateAndTime } from "./general.utils";

export const NEW_USER = {
  login: "",
  email: "",
  password: "",
  role: "",
  organisationsId: [],
};

export const normalizeUsers = (users) => {
  return users?.map((row) => ({
    ...row,
    idTable: row?.id,
    lastLogin: row.lastLogin ? normalizeDateAndTime(row.lastLogin) : null,
    email: row?.email ? row?.email : "test@test.com",
    admin: row?.superAdmin ? "Yes" : "No",
    rolePretty: prettyRole(row?.role)
  }));
};

export const normalizeUsersOutput = (data) => ({
  ...data,
  organisationsId:
    data.organisationsId != null
      ? data.organisationsId.map((org) => org.value)
      : null,
});

export const normalizeUsersInput = (data, organisationsList) => ({
  ...data,
  organisationsId: Array.isArray(data.organisationsId)
    ? organisationsList.filter((org) =>
        data?.organisationsId?.includes(org.value)
      )
    : data.organisationsId,
});

export const USER_ROLES = [
  {value: "ADMIN", label: "Admin"},
  {value: "ADMIN_ORGANISATION", label: "Admin Organisation"},
  {value: "ADMIN_READ_ONLY", label: "Admin read only"},
  {value: "MANAGEMENT", label: "Management"},
  {value: "PARKING_ADMIN", label: "Parking Admin"}
];
export const prettyRole = (role) => {
  if (role == null) {
    return "";
  }
  try {
    return USER_ROLES.filter(ur=>ur.value === role)[0].label;
  } catch (error) {
    return "";
  }
}