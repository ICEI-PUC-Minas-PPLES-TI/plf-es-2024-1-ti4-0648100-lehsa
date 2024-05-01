import React, { useState, useEffect, FormEvent } from "react";
import { Button } from "@/components/ui/button";
import { Label } from "@/components/ui/label";
import { Input } from "@/components/ui/input";
import Cookie from "js-cookie";
import { useRouter } from "next/navigation";
import ValidationMessage from "./ValidationMessage"; // Import the new component

interface User {
  id: string;
  nome: string;
  email: string;
  telefone: string;
  password: string;
  cpf: string;
  perfil_usuario: number;
  [key: string]: string | number; // Index signature to allow any string as key
}

const fieldErrorMessages: { [key: string]: string } = {
  nome: "Por favor, preencha o campo acima.",
  email: "Por favor, preencha o campo acima. (exemplo@email.com)",
  telefone: "Por favor, preencha o campo acima. (xx xxxx-xxxx)",
  password: "Por favor, digite a senha para atualizar os dados.",
};

const MeusDadosForm = () => {
  const [userData, setUserData] = useState<User>({
    id: "",
    nome: "",
    email: "",
    telefone: "",
    password: "",
    cpf: "",
    perfil_usuario: 0,
  });
  const [validationMessages, setValidationMessages] = useState<{
    [key: string]: string;
  }>({});
  const [successMessage, setSuccessMessage] = useState<string>(""); // State for success message
  const router = useRouter();

  useEffect(() => {
    const authToken = Cookie.get("token");

    if (!authToken) {
      // No token found, redirect to login
      router.push("/login");
      return;
    }

    // Decode token to get userId
    const decodedToken = decodeToken(authToken);
    if (!decodedToken || !decodedToken.userId) {
      // Token is invalid or doesn't contain userId, redirect to login
      router.push("/login");
      return;
    }

    // Fetch user data using userId from the decoded token
    fetchUserData(decodedToken.userId, authToken);
  }, []); // Empty dependency array means this runs once on component mount

  const fetchUserData = async (userId: string, authToken: string) => {
    try {
      const response = await fetch(`http://localhost:8080/usuario/${userId}`, {
        method: "GET",
        headers: {
          Authorization: `Bearer ${authToken}`,
          "Content-Type": "application/json",
        },
      });

      if (!response.ok) {
        throw new Error("Error fetching user data");
      }

      const data = await response.json();
      setUserData(data);
    } catch (error) {
      console.error("Error fetching user data:", error);
      // Optionally, handle user feedback or redirection here
    }
  };

  const decodeToken = (token: string) => {
    try {
      const decodedToken = JSON.parse(atob(token.split(".")[1]));
      return decodedToken;
    } catch (error) {
      console.error("Error decoding token:", error);
      return null;
    }
  };

  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault();

    const authToken = Cookie.get("token");
    if (!authToken) {
      // No token found, redirect to login
      router.push("/login");
      return;
    }

    const fieldsToValidate = ["nome", "email", "telefone", "password"]; // List of required fields

    const newValidationMessages: { [key: string]: string } = {}; // Object to store validation messages

    // Check if all required fields are filled
    fieldsToValidate.forEach((field) => {
      if (!userData[field]) {
        newValidationMessages[field] = fieldErrorMessages[field];
      }
    });

    if (Object.keys(newValidationMessages).length > 0) {
      // If there are validation messages, set them and return
      setValidationMessages(newValidationMessages);
      return;
    }

    try {
      const userDataToSend = {
        nome: userData.nome,
        email: userData.email,
        password: userData.password, // assuming you have a password field in userData
        telefone: userData.telefone,
        cpf: userData.cpf,
        perfil_usuario: userData.perfil_usuario,
      };

      const response = await fetch(
        `http://localhost:8080/usuario/${userData.id}`,
        {
          method: "PUT",
          headers: {
            Authorization: `Bearer ${authToken}`,
            "Content-Type": "application/json",
          },
          body: JSON.stringify(userDataToSend),
        }
      );

      if (!response.ok) {
        throw new Error("Error updating user data");
      }

      setSuccessMessage("Dados atualizados com sucesso!"); // Set success message
      console.log("User data updated successfully");
    } catch (error) {
      console.error("Error updating user data:", error);
      // Optionally, handle user feedback or redirection here
    }
  };

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { id, value } = e.target;
    setUserData({
      ...userData,
      [id]: value,
    });

    // Remove validation message for the field being typed in
    setValidationMessages((prevMessages) => {
      const newMessages = { ...prevMessages };
      delete newMessages[id];
      return newMessages;
    });
  };

  return (
    <div className="flex justify-center">
      <div className="flex-col gap-2 mt-10 items-center w-[45rem] rounded-xl">
        <div className="bg-primary text-white text-lg px-3 w-full h-20 rounded-t-xl flex items-center justify-center">
          <div className="flex w-full">
            <h1 className="title">Meus Dados</h1>
          </div>
        </div>

        <div className="bg-white w-full px-10 py-5 rounded-b-xl">
          <form
            className="flex flex-col gap-2 items-center"
            onSubmit={handleSubmit}
          >
            <div className="w-full">
              <Label htmlFor="nome">Nome</Label>
              <Input
                id="nome"
                type="text"
                value={userData.nome}
                onChange={handleChange}
              />
              {validationMessages.nome && (
                <ValidationMessage message={validationMessages.nome} />
              )}
            </div>

            <div className="w-full">
              <Label htmlFor="email">Email</Label>
              <Input
                id="email"
                type="text"
                value={userData.email}
                onChange={handleChange}
              />
              {validationMessages.email && (
                <ValidationMessage message={validationMessages.email} />
              )}
            </div>

            <div className="w-full">
              <Label htmlFor="telefone">Celular</Label>
              <Input
                id="telefone"
                type="text"
                value={userData.telefone}
                onChange={handleChange}
              />
              {validationMessages.telefone && (
                <ValidationMessage message={validationMessages.telefone} />
              )}
            </div>

            <div className="w-full">
              <Label htmlFor="cpf">CPF</Label>
              <Input id="cpf" type="text" value={userData.cpf} readOnly />
            </div>

            <div className="w-full">
              <Label htmlFor="password">Senha</Label>
              <Input
                id="password"
                type="text"
                value={userData.password}
                onChange={handleChange}
              />
              {validationMessages.password && (
                <ValidationMessage message={validationMessages.password} />
              )}
            </div>

            <Button className="mt-3 w-[12rem]" type="submit">
              Salvar
            </Button>
          </form>
          {successMessage && (
            <div className="text-green-600 text-center mt-4">
              {successMessage}
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default MeusDadosForm;
