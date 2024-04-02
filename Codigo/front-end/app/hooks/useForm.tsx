import React, { useState } from "react";

interface Types {
  [key: string]: {
    regex: RegExp;
    message: string;
  };
}

const types: Types = {
  email: {
    regex:
      /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|.(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/,
    message: "Preencha um email válido",
  },
  password: {
    regex: /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z]{8,}$/,
    message:
      "A senha precisa ter 1 caracter maiúsculo, 1 minúsculo e 1 dígito. Com no mínimo 8 caracteres.",
  },
  number: {
    regex: /^\d+$/,
    message: "Utilize números apenas.",
  },
};

interface UseFormReturn {
  value: string;
  setValue: React.Dispatch<React.SetStateAction<string>>;
  onChange: React.ChangeEventHandler<HTMLInputElement>;
  error: string | null;
  validate: (value: string) => boolean;
  onBlur: () => boolean;
}

const useForm = (type: string | false): UseFormReturn => {
  const [value, setValue] = useState("");
  const [error, setError] = useState<string | null>(null);

  function validate(value: string): boolean {
    if (type === false) return true;
    if (value.length === 0) {
      setError("Preencha um valor");
      return false;
    } else if (types[type] && !types[type].regex.test(value)) {
      setError(types[type].message);
      return false;
    } else {
      setError(null);
      return true;
    }
  }

  const onChange: React.ChangeEventHandler<HTMLInputElement> = ({
    target,
  }) => {
    if (error) validate(target.value);
    setValue(target.value);
  };

  const onBlur = () => validate(value);

  return {
    value,
    setValue,
    onChange,
    error,
    validate,
    onBlur,
  };
};

export default useForm;
