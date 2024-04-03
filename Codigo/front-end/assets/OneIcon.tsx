import React from 'react';

interface OneIconProps {
  color: string;
}

const OneIcon: React.FC<OneIconProps> = ({ color }) => (
  <svg
    width="24"
    height="24"
    viewBox="0 0 24 24"
    fill="none"
    xmlns="http://www.w3.org/2000/svg"
  >
    <path
      d="M24 12C24 18.6274 18.6274 24 12 24C5.37258 24 0 18.6274 0 12C0 5.37258 5.37258 0 12 0C18.6274 0 24 5.37258 24 12Z"
      fill={color}
    />
    <path
      d="M13.0479 7.47292V16.2002H11.7268V8.79395H11.6757L9.58764 10.1576V8.89622L11.7652 7.47292H13.0479Z"
      fill="white"
    />
  </svg>
);

export default OneIcon;
