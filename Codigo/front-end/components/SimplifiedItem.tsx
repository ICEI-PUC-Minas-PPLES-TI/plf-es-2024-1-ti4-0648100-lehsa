import React from "react";

import ImageComp from "./ImageComp";

interface Props {
  id: string;
  nome: string;
  quantidade: number;
}

const SimplifiedItem = ({ id, nome, quantidade }: Props) => {
  return (
    <div className="flex items-center space-x-5">
      <ImageComp
        src={`http://localhost:8080/item/img/${id}`}
        alt="item picture"
        width={120}
        height={0}
        className="object-contain rounded-md"
      />
      <div>
        <p className="font-semibold">{nome}</p>
        <p>Quantidade: {quantidade}</p>
      </div>
    </div>
  );
};

export default SimplifiedItem;
