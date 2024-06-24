"use client";

import React from "react";
import Image from "next/image";
import Cookie from "js-cookie";
import { useRouter } from "next/navigation";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuLabel,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from "./ui/dropdown-menu";

type Props = {
  title: string;
};

const TopMenu = ({ title }: Props) => {
  const router = useRouter();
  const LogOut = () => {
    Cookie.remove("token");
    router.push("/");
  };

  return (
    <div className="flex w-full justify-between items-center">
      <h1 className="text-black text-3xl font-semibold">{title}</h1>
      <div className="flex gap-5">
        {/* <Image
          src="/icons/notification.svg"
          width={46}
          height={0}
          alt="notification"
        ></Image> */}
        {/* <Image
          src="/icons/setting.svg"
          width={46}
          height={0}
          alt="setting"
        ></Image> */}
        {/* <div className="text-center">
          <p className="text-sm">
            <span className="font-semibold text-base">Ruth</span>
            <br />
            admin
          </p>
        </div> */}

        <DropdownMenu>
          <DropdownMenuTrigger>
            <Image
              src="/icons/profile.svg"
              width={46}
              height={0}
              alt="profile"
            ></Image>
          </DropdownMenuTrigger>
          <DropdownMenuContent>
            <DropdownMenuItem onClick={() => router.push("/admin/minhaconta")}>
              Minha Conta
            </DropdownMenuItem>
            <DropdownMenuSeparator />
            <DropdownMenuItem
              className="text-red-700 font-medium"
              onClick={LogOut}
            >
              Logout
            </DropdownMenuItem>
          </DropdownMenuContent>
        </DropdownMenu>
      </div>
    </div>
  );
};

export default TopMenu;
