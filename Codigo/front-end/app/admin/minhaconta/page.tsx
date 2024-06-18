"use client";
import React from "react";
import TopMenu from "@/components/topMenu";
import MeusDadosForm from "./MeusDadosForm";

const MinhaConta = () => {
  return (
    <div>
      <TopMenu title="Minha Conta" />
      <MeusDadosForm />
    </div>
  );
};

export default MinhaConta;
