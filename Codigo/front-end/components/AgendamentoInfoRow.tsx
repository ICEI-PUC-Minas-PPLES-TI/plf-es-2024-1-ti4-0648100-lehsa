"use client";
import Cookie from "js-cookie";
import React, { useState, useEffect } from "react";
import { TrashIcon } from "lucide-react";

interface User {
  id: number;
  nome: string;
  email: string;
  telefone: number;
  cpf: string;
  perfil_usuario: number;
}

function AgendamentoInfoRow({ searchTerm }: { searchTerm: string }) {
  const [users, setUsers] = useState<User[]>([]);
  const [token, setToken] = useState<string>("");
  const [loggedInUserId, setLoggedInUserId] = useState<number>(0); // State to store logged-in user ID

  useEffect(() => {
    const authToken = Cookie.get("token") ?? "";
    setToken(authToken);

    fetch(`http://localhost:8080/usuario`, {
      method: "GET",
      headers: {
        Authorization: `Bearer ${authToken}`,
        "Content-Type": "application/json",
      },
    })
      .then((response) => response.json())
      .then((data: User[]) => {
        setUsers(data);
      })
      .catch((error) => {
        console.error("Error fetching users:", error);
      });

    // Decode token to get the ID of the logged-in user
    try {
      const decodedToken = JSON.parse(atob(authToken.split(".")[1]));
      if (decodedToken && decodedToken.userId) {
        setLoggedInUserId(decodedToken.userId);
      }
    } catch (error) {
      console.error("Error decoding token:", error);
    }
  }, []);

  const renderProfileType = (perfil_usuario: number) => {
    if (perfil_usuario === 1) {
      return "ADM";
    } else if (perfil_usuario === 2) {
      return "Usuário";
    } else {
      return "Erro";
    }
  };

  // const filteredUsers = users.filter((user) =>
  //   user.nome.toLowerCase().includes(searchTerm.toLowerCase())
  // );

  return (
    <div className="flex justify-center">
      <div className="table w-[21rem] rounded-xl">
        <div className="table-header-group">
          <div className="table-row h-[2rem]">
            <div className="table-cell text-left pl-5 font-medium">Dia</div>
            <div className="table-cell text-left pl-5 font-medium">Técnico responsável</div>
            <div className="table-cell text-left pl-5 font-medium">Contato</div>
            <div className="table-cell text-left pl-5 font-medium"></div>
          </div>
        </div>

        {/* {filteredUsers.map((user, index) => (
          <ul
            className={`usersTable table-row-group ${
              index % 2 === 0 ? "evenRow" : "oddRow"
            }`}
            data-id={user.id}
            key={user.id}
          >
            <li className="table-cell text-left h-[3rem] align-middle pl-5">
              <p>{user.nome}</p>
            </li>
            <li className="table-cell text-left h-[3rem] align-middle pl-5">
              <p>{user.email}</p>
            </li>
            <li className="table-cell text-left h-[3rem] align-middle pl-5">
              <p>{user.telefone}</p>
            </li>
            <li className="table-cell text-left h-[3rem] align-middle pl-5 ">
            </li>
          </ul>
        ))} */}
      </div>
    </div>
  );
}

export default AgendamentoInfoRow;
