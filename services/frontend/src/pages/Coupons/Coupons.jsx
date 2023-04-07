import { useState, useEffect, useContext } from "react";
import { useTranslation } from "react-i18next";
import { useCouponService } from "hooks/useCouponService";
import toast from "react-hot-toast";
import { AuthContext } from "context";
import {
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
} from "@mui/material";
import {
  allowActionBaseOnRole,
  COUPONS_LIST_COLUMNS,
  DEFAULT_COUPON_FILTER,
  screens,
  userActions,
} from "utils";
import {
  filterActiveSelectFilter,
  filterSearchText,
  FILTER_ACTIVE_VALUES,
} from "utils/filter.utils";
import { useForm, Controller } from "react-hook-form";
import { normalizeCoupons } from "utils/coupons.utils";
import CouponForm from "components/forms/CouponForm/CouponForm";
import { getErrorMessageHttp } from "utils/errors.utils";

import "./coupons.scss";
import Card from "../../components/UI/Card/Card";
import Input from "../../components/UI/Input/Input";
import Select from "../../components/UI/Select/Select";
import Table from "../../components/UI/Table/Table";
import Button from "../../components/UI/Button/Button";
import Loader from "../../components/UI/Loader/Loader";

const Coupons = () => {
  const [couponFormErrors, setCouponFormErrors] = useState("");
  const { t } = useTranslation();
  const [couponList, setCouponList] = useState([]);
  const [filteredCouponList, setFilteredCouponList] = useState([]);
  const { role, superAdmin } = useContext(AuthContext);
  const { getAllCoupons, getCouponById, createCoupon, updateCoupon } =
    useCouponService();
  const [openDialog, setOpenDialog] = useState(false);
  const [selectedCoupon, setSelectedCoupon] = useState(null);
  const [loading, setLoading] = useState(false);

  const couponsFilterForm = useForm({
    mode: "onChange",
    defaultValues: DEFAULT_COUPON_FILTER,
  });

  useEffect(() => {
    setLoading(true);
    getAllCoupons().then((coupons) => {
      setCouponList(coupons);
      filterCoupons(coupons);
      setLoading(false);
    });
  }, []); // eslint-disable-line react-hooks/exhaustive-deps

  const filterCoupons = (coupons) => {
    const activeFilter = couponsFilterForm.getValues("active");
    const textSearchFilter = couponsFilterForm.getValues("textSearch");
    let filteredCoupons = coupons;
    //active
    filteredCoupons = filterActiveSelectFilter(activeFilter, filteredCoupons);
    //searchText
    filteredCoupons = filterSearchText(
      COUPONS_LIST_COLUMNS,
      textSearchFilter,
      filteredCoupons
    );
    //set filteredData
    setFilteredCouponList(
      normalizeCoupons({
        coupons: filteredCoupons,
        superAdmin,
        onEdit: onEditCoupon,
      })
    );
  };

  const onCreateCoupon = () => {
    setCouponFormErrors(null);
    setOpenDialog(true);
    setSelectedCoupon(null);
  };

  const onEditCoupon = (event) => {
    getCouponById(event.target.id).then((coupon) => {
      setCouponFormErrors(null);
      setOpenDialog(true);
      setSelectedCoupon(coupon);
    }).catch(err => {
      toast.error("Something went wrong getting coupon. try it later.");
    });
  };

  const onCloseDialog = () => {
    setOpenDialog(false);
  };

  const onSubmitCoupon = async (couponSubmit) => {
    const edit = couponSubmit?.id != null ? true : false;
    try {
      if (edit) {
        await updateCoupon(couponSubmit);
      } else {
        await createCoupon(couponSubmit);
      }
      toast.success(
        `Coupon has been successfully ${edit ? "created" : "edited"}.`
      );
      setCouponFormErrors(null);
      setOpenDialog(false);
      getAllCoupons().then((coupons) => {
        setCouponList(coupons);
        filterCoupons(coupons);
      });
    } catch (err) {
      setCouponFormErrors(getErrorMessageHttp(err));
    }
  };

  const actions = {
    editAction: allowActionBaseOnRole(screens.coupons, role, userActions.edit),
    onEdit: onEditCoupon,
  };

  return (
    <div className="screen__wrapper">
      <p className="users-screen__description">{t("coupons.description")}</p>
      <p className="users-screen__title">{t("coupons.title")}</p>
      <Card>
        <div className="users-screen__card--actions-bar">
          {allowActionBaseOnRole(screens.coupons, role, userActions.create) ? (
            <Button text="Create Coupon" handleOnClick={onCreateCoupon} />
          ) : null}

          <Controller
            name="textSearch"
            control={couponsFilterForm.control}
            render={({ field }) => (
              <Input
                label="Search coupon..."
                onChange={(value) => {
                  field.onChange(value);
                  filterCoupons(couponList);
                }}
              />
            )}
          />
          <Controller
            name="active"
            control={couponsFilterForm.control}
            render={({ field }) => (
              <Select
                label="Active"
                data={FILTER_ACTIVE_VALUES}
                fullWidth={false}
                style={{ marginLeft: "1em" }}
                {...field}
                onChange={(value) => {
                  field.onChange(value);
                  filterCoupons(couponList);
                }}
              />
            )}
          />
        </div>

        {loading ? (
          <Loader />
        ) : ""}
        <Table
            columns={COUPONS_LIST_COLUMNS}
            rows={filteredCouponList}
            superAdmin={true}
            actions={actions}
            role={"mainTable"}
          />
      </Card>
      <Dialog open={openDialog} onClose={onCloseDialog} maxWidth="false">
        <DialogTitle>
          <div className="d-flex justify-content-between">
            <div>{selectedCoupon?.id ? "Edit" : "Create"} Coupon</div>
            <div className="clicable" onClick={onCloseDialog}>
              <i className="bi bi-x"></i>
            </div>
          </div>
        </DialogTitle>
        <DialogContent>
          <CouponForm
            sourceCoupon={selectedCoupon}
            onSubmit={onSubmitCoupon}
            errors={couponFormErrors}
          />
        </DialogContent>
        <DialogActions></DialogActions>
      </Dialog>
    </div>
  );
};

export default Coupons;
