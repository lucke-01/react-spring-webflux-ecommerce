import { useState, useEffect } from "react";
import { Controller, useForm } from "react-hook-form";
import { DataGrid } from '@mui/x-data-grid';
import PropTypes from 'prop-types';
import { AdapterDayjs } from "@mui/x-date-pickers/AdapterDayjs";
import { LocalizationProvider } from "@mui/x-date-pickers/LocalizationProvider";
import { Alert, Grid } from "@mui/material";
import Dialog from '@mui/material/Dialog';
import DialogContent from '@mui/material/DialogContent';
import DialogTitle from '@mui/material/DialogTitle';
import {
  normalizeDateToUTC,
  validateStartAndEndDate,
  RULE_REQUIRED,
  RULE_POSITIVE_NUMBER,
  WEEK_DAYS_CHECKBOX,
  RULE_VALID_DECIMAL_NUMBER
} from "utils";

import "./CouponForm.scss";
import { useLocationService } from "hooks/useLocationService";
import { useProductService } from "hooks/useProductService";
import { DEFAULT_COUPON, DISCOUNT_TYPES } from "utils/coupons.utils";
import SearchSelector from "components/UI/SearchSelector/SearchSelector";
import Button from "../../UI/Button/Button";
import Input from "../../UI/Input/Input";
import Select from "../../UI/Select/Select";
import DatePicker from "../../UI/DatePicker/DatePicker";
import Checkbox from "../../UI/Checkbox/Checkbox";

const CouponForm = ({sourceCoupon, onSubmit, errors}) => {
  const [selectionModel, setSelectionModel] = useState([])
  const DATA_COLUMN_NAME = [{accessorKey: "name", header: "Name"}];
  const [ selectedLocation, setSelectedLocation ] = useState({selectedProducts: []});
  const [ locationSelectedList, setLocationSelectedList ] = useState([]);
  const [ availableLocationProducts, setAvailableLocationProducts ] = useState([]);
  const { getAllLocation } = useLocationService();
  const { getProductByLocationId } = useProductService();
  const [sending, setSending] = useState(false);
  const [error, setError] = useState(null);
  const [locationsList, setLocationsList] = useState([]);
  const [openLocationSelectorDialog, setOpenLocationSelectorDialog] = useState(false);
  const [openProductSelectorDialog, setOpenProductSelectorDialog] = useState(false);

  const LOCATION_SELECTION_COLUMNS = [
    {
      field: 'selection',
      headerName: 'Sel.',
      align:'center',
      width: 50,
      renderCell: (props) => {
        const selected = props.id === selectionModel[0];

        return <div><input readOnly={true} checked={selected} type="radio" /></div>;
      },
    },
    { field: 'name', headerName: 'Name', width: 200 },
    {
      field: 'actions',
      headerName: 'Actions',
      type: 'actions',
      getActions: (params) => [
        <button style={{height: '2em'}} onClick={ () => deleteSelectedLocation(params.id)} type="button" className="clicable"><i className="bi bi-x"></i></button>
      ],
    }
  ];
  const PRODUCT_SELECTION_COLUMNS = [
    { field: 'name', headerName: 'Name', width: 200 },
    {
      field: 'actions',
      headerName: 'Actions',
      type: 'actions',
      getActions: (params) => [
        <button style={{height: '2em'}} onClick={ () => deleteSelectedProduct(params.id)} type="button" className="clicable"><i className="bi bi-x"></i></button>
      ],
    }
  ];

  const couponForm = useForm({
    mode: "onSubmit",
    defaultValues: DEFAULT_COUPON
  });
  useEffect(() => {
    getAllLocation().then(locations => {
      setLocationsList(locations);
    });
    // eslint-disable-next-line 
  }, []);
  useEffect(() => {
    if (sourceCoupon != null) {
      Object.keys(sourceCoupon).forEach((key) => {
          couponForm.setValue(key, sourceCoupon[key])
        }
      );
      if (locationsList?.length > 0) {
        setLocationSelectedList(desNormalizeLocationSelectedList(sourceCoupon.locationProducts));
      }
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [sourceCoupon, locationsList]);

  useEffect(() => {
      setError(errors);
    // eslint-disable-next-line 
  }, [errors]);
  const onSubmitForm = (form) => {
    //check selected days
    let daysChecked = Object.entries(form.dayOfWeek).filter(([key, value]) => form.dayOfWeek[key] === true);
    if (daysChecked.length === 0) {
      couponForm.setError("dayOfWeek", {type: "manual", message: "Choose at least one day"});
      return;
    }
    setSending(true);
    const locationProducts = normalizeLocationSelectedList(locationSelectedList);
    onSubmit({...form, locationProducts}).then(data => {
      setSending(false);
    });
  };
  const onCloseLocationSelectorDialog = () => {
    setOpenLocationSelectorDialog(false);
  }
  const openAddLocation = () => {
    setOpenLocationSelectorDialog(true);
  }
  const onSelectLocationSearch = (selection) => {
    onCloseLocationSelectorDialog();
    const normalizedSelection = normalizeLocationSelection(selection, locationsList);
    setLocationSelectedList([...locationSelectedList, ...normalizedSelection]);
  }
  const normalizeLocationSelection = (idLocations, locationsList) => {
    let idLocationsUnique = idLocations.filter(idLoc => !locationSelectedList.map(loc=>loc.id).includes(idLoc));
    let normalizedSelection = [];
    locationsList.forEach(loc => {
      if (idLocationsUnique.includes(loc.id)) {
        normalizedSelection.push({...loc, selectedProducts: [], allProducts: false});
      }
    });
    return normalizedSelection;
  }
  const onLocationSelectedChange = (selectedLocationId) => {
    const firstSelectedLocationId = selectedLocationId[0];
    getProductByLocationId(firstSelectedLocationId).then(products => {
      setAvailableLocationProducts(products);

      let newLocationSelectedList = normalizeSelectedProducts(firstSelectedLocationId, products);
      setLocationSelectedList(newLocationSelectedList);
      
      let newSelectedLocation = newLocationSelectedList.find(loc => {return loc.id === firstSelectedLocationId});
      setSelectedLocation(newSelectedLocation);
    });
  }
  const normalizeSelectedProducts = (selectedLocationId, products) => {
    return locationSelectedList
      .map(loc => 
        (
          loc.id === selectedLocationId ? 
            {...loc, 
              selectedProducts: loc.selectedProducts.map(sp => {return products.find(p => {return p.id === sp.id}) }) 
            } : loc
        )
      );
  }

  const openAddProduct = () => {
    if (selectedLocation  != null) {
      setOpenProductSelectorDialog(true);
    }
  }
  const onSelectProductSearch = (selection) => {
    onCloseProductSelectorDialog();
    const newLocationSelectedList = [...locationSelectedList];
    const selectedIndex = newLocationSelectedList.findIndex(loc => loc.id === selectedLocation.id);
    const locationSelected = newLocationSelectedList[selectedIndex];
    const normalizedSelection = normalizeProductSelection(selection, locationSelected, availableLocationProducts);

    locationSelected.selectedProducts = [...locationSelected.selectedProducts, ...normalizedSelection];

    modifyLocationSelectedList(newLocationSelectedList);
  }
  const normalizeProductSelection = (idProducts, locationSelected , availableLocationProducts) => {
    let idProductsUnique = idProducts.filter(idProduct => !locationSelected.selectedProducts.map(product=>product.id).includes(idProduct));
    let normalizedSelection = [];
    availableLocationProducts.forEach(product => {
      if (idProductsUnique.includes(product.id)) {
        normalizedSelection.push(product);
      }
    });
    return normalizedSelection;
  }
  const onCloseProductSelectorDialog = () => {
    setOpenProductSelectorDialog(false);
  }
  const deleteSelectedLocation = (idLocation) => {
    if (selectedLocation != null) {
      if (idLocation === selectedLocation.id) {
        setSelectedLocation(null);
      }
    }
    const newLocationSelectList = locationSelectedList.filter(loc => loc.id !== idLocation);
    setLocationSelectedList(newLocationSelectList);
  }
  const deleteSelectedProduct = (idProduct) => {
    if (selectedLocation != null) {

      const newLocationSelectList = [...locationSelectedList];
      const selectedIndex = newLocationSelectList.findIndex(loc => loc.id === selectedLocation.id);
      const locationSelected = newLocationSelectList[selectedIndex];
      locationSelected.selectedProducts = locationSelected.selectedProducts.filter(product => product.id !== idProduct);

      modifyLocationSelectedList(newLocationSelectList);
    }
  }
  const onChangeAllProducts = (event) => {
    const value = event.target.checked;
    const newLocationSelectList = locationSelectedList.map(loc => loc.id === selectedLocation.id ? {...loc, allProducts: value} : loc);

    modifyLocationSelectedList(newLocationSelectList);
  }
  const normalizeLocationSelectedList = (locationSelectedList) => {
    return locationSelectedList
        .map(loc => ({idLocation: loc.id, idProducts: loc.selectedProducts.map(p => p.id), allProducts: loc.allProducts}));
  }
  const desNormalizeLocationSelectedList = (locationProducts) => {
    return locationProducts.map(locProduct => 
      ({
        ...locationsList.find(loc => loc.id === locProduct.idLocation),
        selectedProducts: locProduct.idProducts?.map(idProduct => ({id: idProduct})),
        allProducts: locProduct.allProducts
      }));
  }
  const modifyLocationSelectedList = (newLocationSelectedList) => {
    setLocationSelectedList(newLocationSelectedList);
    if (selectedLocation != null) {
      let newSelectedLocation = newLocationSelectedList.find(loc => loc.id === selectedLocation.id);
      setSelectedLocation(newSelectedLocation);
    }
  }

  return (
    <form onSubmit={couponForm.handleSubmit(onSubmitForm)} style={{marginTop: "1em"}}>
      <div style={{margin: '1em'}}>
        {error ? <Alert severity="warning">{error}</Alert> : null}
      </div>
      <div>
      <Grid container alignItems="top">
        <Grid item xs={8}>
          <div className="grid-2 spaced-form">
            <LocalizationProvider dateAdapter={AdapterDayjs}>
              <Input
                {...couponForm.register("name", { ...RULE_REQUIRED })}
                label="Name"
                InputLabelProps={{ shrink: true }}
                error={!!couponForm.formState.errors?.name}
                helperText={couponForm.formState.errors?.name?.message}
              />
              <Input
                {...couponForm.register("code", { ...RULE_REQUIRED })}
                label="Code"
                InputLabelProps={{ shrink: true }}
                error={!!couponForm.formState.errors?.code}
                helperText={couponForm.formState.errors?.code?.message}
              />
              <Controller
                  name="activeFrom"
                  control={couponForm.control}
                  rules={{
                    ...RULE_REQUIRED,
                    validate: {
                      startEndDate: (value) =>
                        validateStartAndEndDate(
                          value,
                          couponForm.getValues("activeTo"),
                          "active from should not be greater than activeTo",
                          () => {
                            couponForm.clearErrors("activeTo");
                          }
                        ),
                    },
                  }}
                  render={({ field }) => (
                    <DatePicker
                      {...field}
                      label="Active from"
                      value={field.value}
                      error={!!couponForm.formState.errors?.activeFrom}
                      helperText={couponForm.formState.errors?.activeFrom?.message}
                      onChange={(date) => {
                        field.onChange(normalizeDateToUTC(date));
                      }}
                    />
                  )}
                />
                <Controller
                  name="activeTo"
                  control={couponForm.control}
                  rules={{
                    ...RULE_REQUIRED,
                    validate: {
                      startEndDate: (value) =>
                        validateStartAndEndDate(
                          couponForm.getValues("activeFrom"),
                          value,
                          "active to should be greater than active from",
                          () => {
                            couponForm.clearErrors("activeFrom");
                          }
                        ),
                    },
                  }}
                  render={({ field }) => (
                    <DatePicker
                      {...field}
                      label="Active to"
                      value={field.value}
                      error={!!couponForm.formState.errors?.activeTo}
                      helperText={couponForm.formState.errors?.activeTo?.message}
                      onChange={(date) => {
                        field.onChange(normalizeDateToUTC(date));
                      }}
                    />
                  )}
                />
                <Grid container alignItems="center">
                  <Grid item xs={8}>
                    <Input
                      {...couponForm.register("discount", 
                          { ...RULE_REQUIRED, ...RULE_POSITIVE_NUMBER,
                             validate: {
                              ...RULE_VALID_DECIMAL_NUMBER
                             }
                          })
                      }
                      InputLabelProps={{ shrink: true }}
                      label="Discount"
                      error={!!couponForm.formState.errors?.discount}
                      helperText={couponForm.formState.errors?.discount?.message}
                    />
                  </Grid>
                  <Grid item xs={4}>
                    <Controller
                      name="discountType"
                      control={couponForm.control}
                      rules={{ ...RULE_REQUIRED }}
                      render={({ field }) => (
                        <Select
                          label="Discount Type"
                          data={DISCOUNT_TYPES}
                          error={!!couponForm.formState.errors?.discountType}
                          helperText={couponForm.formState.errors?.discountType?.message}
                          {...field}
                        />
                      )}
                    />
                  </Grid>
                </Grid>

                <Input
                  {...couponForm.register("shortDescription", { ...RULE_REQUIRED })}
                  label="Short Description"
                  InputLabelProps={{ shrink: true }}
                  error={!!couponForm.formState.errors?.shortDescription}
                  helperText={couponForm.formState.errors?.shortDescription?.message}
                />
              <Controller
              name="active"
              control={couponForm.control}
              render={({ field }) => (
                <Checkbox label="Active" {...field} />
              )}
            />
            </LocalizationProvider>
          </div>
        </Grid>
        <Grid item xs={1}></Grid>
        <Grid item xs={3}>
          <label key="labelDaysOfWeek" style={{fontSize: '13px'}}>Days of week</label>
          <div className="rates-form__inputs--days" style={{marginLeft: '1em'}}>
            {WEEK_DAYS_CHECKBOX.map(({ key }) => (
              <div>
              <Controller
                key={key}
                name={`dayOfWeek.${key.toLowerCase()}`}
                control={couponForm.control}
                render={({ field }) => (
                  <Checkbox
                    label={key.toLowerCase()}
                    {...field}
                    onChange={(e) => {
                      couponForm.clearErrors("dayOfWeek");
                      field.onChange(e);
                    }}
                  />
                )}
              />
              </div>
            ))}
            <p style={{ marginLeft: 0 }}
                className="text-error-helper">
              {couponForm.formState.errors?.dayOfWeek?.message}
            </p>
          </div>
        </Grid>
      </Grid>
      <div className="locations-products">
        <h4 style={{marginBottom: '0.5em'}}>Locations and Products</h4>
        <Grid container alignItems="top">
          <Grid item xs={5}>
            <div style={{fontSize: "1em"}} className="d-flex justify-content-between">
              <div>
                <strong>Locations</strong>
              </div>
              <div>
                <div className="clicable icon-add" onClick={openAddLocation}><i className="bi-plus-square" style={{fontSize: "1.5em"}}></i></div>
              </div>
            </div>
            <div style={{marginBottom: '0.5em'}}>
                Select Locations and its product to apply coupon
              </div>
            <div>
              <DataGrid
                getRowId={(row) => row.id }
                rows={locationSelectedList}
                columns={LOCATION_SELECTION_COLUMNS}
                pageSize={5}
                selectionModel={selectionModel}
                hideFooterSelectedRowCount
                onSelectionModelChange={(selection) => {
                  onLocationSelectedChange(selection);
                  setSelectionModel(selection);
                }}
                autoHeight={true}
              />
            </div>
          </Grid>
          <Grid item xs={1}></Grid>
          <Grid item xs={5}>
            <div style={{fontSize: "1em"}} className="d-flex justify-content-between">
              <div>
                <strong>Products</strong>
              </div>
              <div>
                <div className={`clicable ${selectedLocation.id != null ? 'icon-add' : 'icon-disable'}`} onClick={openAddProduct}>
                  <i className="bi-plus-square" style={{fontSize: "1.5em"}}></i>
                </div>
              </div>
            </div>
            <div>
              <div style={{marginBottom: '0.5em'}} className="clicable">
                <input id="allProducts" type="checkbox" checked={selectedLocation?.allProducts} onChange={onChangeAllProducts} />
                <label htmlFor="allProducts" style={{fontSize: '14px'}}> All Product</label>
              </div>
            </div>
            <div>              
              <DataGrid
                getRowId={(row) => row.id }
                rows={selectedLocation.selectedProducts}
                columns={PRODUCT_SELECTION_COLUMNS}
                pageSize={5}
                autoHeight={true}
              />
            </div>
          </Grid>
        </Grid>
      </div>

      </div>
      <div className="coupon-form__buttons">
        <Button disabled={sending} text={!sending ? "Save" : "Processing..."} />
      </div>

      <Dialog open={openLocationSelectorDialog} onClose={onCloseLocationSelectorDialog} fullWidth={ true }>
        <DialogTitle>
          <div className="d-flex justify-content-between">
            <div>Choose Location</div>
            <div className="clicable" onClick={onCloseLocationSelectorDialog}><i className="bi bi-x"></i></div>
          </div>
        </DialogTitle>
        <DialogContent>
          <SearchSelector dataList={locationsList} dataColumns={DATA_COLUMN_NAME} selectRow={onSelectLocationSearch} multiSelection={true} />
        </DialogContent>
      </Dialog>

      <Dialog open={openProductSelectorDialog} onClose={onCloseProductSelectorDialog} fullWidth={ true }>
        <DialogTitle>
          <div className="d-flex justify-content-between">
            <div>Choose Product</div>
            <div className="clicable" onClick={onCloseProductSelectorDialog}><i className="bi bi-x"></i></div>
          </div>
        </DialogTitle>
        <DialogContent>
          <SearchSelector dataList={availableLocationProducts} dataColumns={DATA_COLUMN_NAME} selectRow={onSelectProductSearch} multiSelection={true} />
        </DialogContent>
      </Dialog>
    </form>
  );
};

CouponForm.propTypes = {
  sourceCoupon: PropTypes.object,
  onSubmit: PropTypes.func.isRequired,
  errors: PropTypes.string
};

export default CouponForm;