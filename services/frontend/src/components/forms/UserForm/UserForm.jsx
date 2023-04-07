import { useEffect, useState } from "react";
import { Controller, useForm } from "react-hook-form";
import {
  NEW_USER,
  normalizeUsersInput,
  normalizeUsersOutput,
  RULE_REQUIRED,
  USER_ROLES,
} from "utils";
import "./UserForm.scss";
import { useOrganisationService } from "hooks/useOrganisationService";
import Input from "../../UI/Input/Input";
import Select from "../../UI/Select/Select";
import Button from "../../UI/Button/Button";

const UserForm = ({ onClick, initialState, isEdit }) => {
  const [user, setUser] = useState({});
  const [sending, setSending] = useState(false);

  const {getOrganisationsSelectList} = useOrganisationService();
  const [organisationsList, setOrganisationsList] = useState([]);
  const {
    control,
    register,
    handleSubmit,
    setValue,
    formState: { errors }
  } = useForm({
    mode: "onSubmit",
    defaultValues: NEW_USER,
  });
  useEffect(() => {
    getOrganisationsSelectList().then(organisationsSelect => {
      setOrganisationsList(organisationsSelect);
    });
    // eslint-disable-next-line 
  }, []);
  useEffect(() => {
    if (initialState) {
      Object.keys(initialState).forEach((key) =>
        setValue(key, normalizeUsersInput(initialState, organisationsList)[key])
      );
      setUser(normalizeUsersInput(initialState, organisationsList));
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [initialState, organisationsList]);

  const onSubmit = (data) => {
    const newUser = { ...data, organisationsId: user.organisationsId };
    const newNormalizedUser = normalizeUsersOutput(newUser);

    setSending(true);
    onClick(newNormalizedUser).then(data => {
      setSending(false);
    });
  };
  const onSelectUserRol = (event) => {
  };

  return (
    <form className="user-form" onSubmit={handleSubmit(onSubmit)}>
      <div className="user-form__inputs">
        <Input
          {...register("email", { ...RULE_REQUIRED })}
          type="email"
          label="Email"
          error={!!errors?.email}
          helperText={errors?.email?.message}
        />

        <Input
          {...register("login", { ...RULE_REQUIRED })}
          label="Username"
          error={!!errors?.login}
          helperText={errors?.login?.message}
        />

        <Input
          {...register("password", isEdit ? {} : { ...RULE_REQUIRED })}
          type="password"
          label="Password"
          error={!!errors?.password}
          helperText={errors?.password?.message}
        />

        <Controller
          name="role"
          control={control}
          rules={{ ...RULE_REQUIRED }}
          render={({ field }) => (
            <Select
              label="User Role"
              data={USER_ROLES}
              error={!!errors?.role}
              helperText={errors?.role?.message}
              {...field}
              onChange={(value) => {
                field.onChange(value);
                onSelectUserRol(value);
              }}
            />
          )}
        />
      </div>
      <div className="user-form__buttons">
        <Button disabled={sending} text={!sending ? "Save" : "Saving..."} />
      </div>
    </form>
  );
};

export default UserForm;
