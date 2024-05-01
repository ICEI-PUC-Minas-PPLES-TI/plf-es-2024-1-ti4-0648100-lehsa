"use client";
import React, { useState, useEffect, FormEvent } from "react";
import TopMenu from "@/components/topMenu";
import { Button } from "@/components/ui/button";
import { Label } from "@radix-ui/react-label";
import { Input } from "@/components/ui/input";
import Cookie from "js-cookie";
import { useRouter } from "next/navigation";
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
