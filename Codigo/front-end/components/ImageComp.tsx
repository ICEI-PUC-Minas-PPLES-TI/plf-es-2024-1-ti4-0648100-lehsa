import React, { useEffect, useState } from 'react';
import Image from 'next/image';
import Cookie from 'js-cookie';

interface ImageCompProps {
    src: string;
    alt: string;
    width: number;
    height: number;
    className: string;
}

const ImageComp: React.FC<ImageCompProps> = ({ src, alt, width, height, className }) => {
    const [imageUrl, setImageUrl] = useState<string>('');

    useEffect(() => {
        const token = Cookie.get("token");
        if (token) {
            fetch(src, {
                headers: {
                    Authorization: `Bearer ${token}`
                }
            })
            .then(response => response.blob())
            .then(blob => {
                const localUrl = URL.createObjectURL(blob);
                setImageUrl(localUrl);
            })
            .catch(error => console.error('Erro carregar imagem:', error));
        }
    }, [src]);

    return (
        imageUrl ? 
            <Image src={imageUrl} alt={alt} width={width} height={height} className={className}  />
            : <p className='p-6'>Carregando...</p>
    );
};

export default ImageComp;
