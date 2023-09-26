const useIcon = () => {
  const IcMenu = ({ size, color }: { size: number; color: string }) => {
    return (
      <svg
        width={`${size}rem`}
        height={`${size}rem`}
        viewBox="0 0 30 30"
        fill="none"
        xmlns="http://www.w3.org/2000/svg"
      >
        <path
          fillRule="evenodd"
          clipRule="evenodd"
          d="M4.5 7.5C4.5 7.10218 4.65804 6.72064 4.93934 6.43934C5.22064 6.15804 5.60218 6 6 6H24C24.3978 6 24.7794 6.15804 25.0607 6.43934C25.342 6.72064 25.5 7.10218 25.5 7.5C25.5 7.89782 25.342 8.27936 25.0607 8.56066C24.7794 8.84196 24.3978 9 24 9H6C5.60218 9 5.22064 8.84196 4.93934 8.56066C4.65804 8.27936 4.5 7.89782 4.5 7.5ZM4.5 15C4.5 14.6022 4.65804 14.2206 4.93934 13.9393C5.22064 13.658 5.60218 13.5 6 13.5H24C24.3978 13.5 24.7794 13.658 25.0607 13.9393C25.342 14.2206 25.5 14.6022 25.5 15C25.5 15.3978 25.342 15.7794 25.0607 16.0607C24.7794 16.342 24.3978 16.5 24 16.5H6C5.60218 16.5 5.22064 16.342 4.93934 16.0607C4.65804 15.7794 4.5 15.3978 4.5 15ZM4.5 22.5C4.5 22.1022 4.65804 21.7206 4.93934 21.4393C5.22064 21.158 5.60218 21 6 21H24C24.3978 21 24.7794 21.158 25.0607 21.4393C25.342 21.7206 25.5 22.1022 25.5 22.5C25.5 22.8978 25.342 23.2794 25.0607 23.5607C24.7794 23.842 24.3978 24 24 24H6C5.60218 24 5.22064 23.842 4.93934 23.5607C4.65804 23.2794 4.5 22.8978 4.5 22.5Z"
          fill={color}
        />
      </svg>
    )
  }
  const IcSearch = ({ size, color }: { size: number; color: string }) => {
    return (
      <svg
        width={`${size}rem`}
        height={`${size}rem`}
        viewBox="0 0 24 24"
        fill="none"
        xmlns="http://www.w3.org/2000/svg"
      >
        <circle cx="11" cy="11" r="6" stroke={color} />
        <path d="M20 20L17 17" stroke={color} strokeLinecap="round" />
      </svg>
    )
  }

  const IcBell = ({ size, color }: { size: number; color: string }) => {
    return (
      <svg
        width={`${size}rem`}
        height={`${size}rem`}
        viewBox="0 0 24 24"
        fill="none"
        xmlns="http://www.w3.org/2000/svg"
      >
        <path
          d="M6.44784 8.96942C6.76219 6.14032 9.15349 4 12 4V4C14.8465 4 17.2378 6.14032 17.5522 8.96942L17.804 11.2356C17.8072 11.2645 17.8088 11.279 17.8104 11.2933C17.9394 12.4169 18.3051 13.5005 18.8836 14.4725C18.8909 14.4849 18.8984 14.4973 18.9133 14.5222L19.4914 15.4856C20.0159 16.3599 20.2782 16.797 20.2216 17.1559C20.1839 17.3946 20.061 17.6117 19.8757 17.7668C19.5971 18 19.0873 18 18.0678 18H5.93223C4.91268 18 4.40291 18 4.12434 17.7668C3.93897 17.6117 3.81609 17.3946 3.77841 17.1559C3.72179 16.797 3.98407 16.3599 4.50862 15.4856L5.08665 14.5222C5.10161 14.4973 5.10909 14.4849 5.11644 14.4725C5.69488 13.5005 6.06064 12.4169 6.18959 11.2933C6.19123 11.279 6.19283 11.2645 6.19604 11.2356L6.44784 8.96942Z"
          stroke={color}
        />
        <path
          d="M9.10222 18.4059C9.27315 19.1501 9.64978 19.8077 10.1737 20.2767C10.6976 20.7458 11.3396 21 12 21C12.6604 21 13.3024 20.7458 13.8263 20.2767C14.3502 19.8077 14.7269 19.1501 14.8978 18.4059"
          stroke={color}
          strokeLinecap="round"
        />
      </svg>
    )
  }

  const IcHomeLight = ({ size, color }: { size: number; color: string }) => {
    return (
      <svg
        width={`${size}rem`}
        height={`${size}rem`}
        viewBox="0 0 32 32"
        fill="none"
        xmlns="http://www.w3.org/2000/svg"
      >
        <path
          d="M6.66663 16.6175C6.66663 14.8493 6.66663 13.9651 7.03257 13.1879C7.39852 12.4108 8.08578 11.8354 9.4603 10.6846L10.7936 9.56829C13.2781 7.48826 14.5203 6.44824 16 6.44824C17.4797 6.44824 18.7219 7.48826 21.2063 9.56829L22.5396 10.6846C23.9141 11.8354 24.6014 12.4108 24.9673 13.1879C25.3333 13.9651 25.3333 14.8493 25.3333 16.6175V22.1401C25.3333 24.5958 25.3333 25.8237 24.5522 26.5866C23.7712 27.3495 22.5141 27.3495 20 27.3495H12C9.4858 27.3495 8.22872 27.3495 7.44767 26.5866C6.66663 25.8237 6.66663 24.5958 6.66663 22.1401V16.6175Z"
          stroke={color}
        />
        <path
          d="M19.3333 27.3496V20.5354C19.3333 19.9831 18.8856 19.5354 18.3333 19.5354H13.6666C13.1143 19.5354 12.6666 19.9831 12.6666 20.5354V27.3496"
          stroke={color}
          strokeLinecap="round"
          strokeLinejoin="round"
        />
      </svg>
    )
  }

  const IcFavoriteLight = ({
    size,
    color,
  }: {
    size: number
    color: string
  }) => {
    return (
      <svg
        width={`${size}rem`}
        height={`${size}rem`}
        viewBox="0 0 32 32"
        fill="none"
        xmlns="http://www.w3.org/2000/svg"
      >
        <path
          d="M5.93427 18.5442L15.3154 27.3567C15.6398 27.6615 15.802 27.8139 16 27.8139C16.1981 27.8139 16.3603 27.6615 16.6847 27.3567L26.0658 18.5442C28.674 16.094 28.9908 12.0621 26.7971 9.23467L26.3846 8.70304C23.7604 5.32069 18.4929 5.88794 16.6489 9.75145C16.3885 10.2972 15.6116 10.2972 15.3512 9.75145C13.5072 5.88794 8.23967 5.32069 5.61543 8.70304L5.20296 9.23467C3.00931 12.0621 3.32604 16.094 5.93427 18.5442Z"
          stroke={color}
        />
      </svg>
    )
  }

  const IcPinLight = ({ size, color }: { size: number; color: string }) => {
    return (
      <svg
        width={`${size}rem`}
        height={`${size}rem`}
        viewBox="0 0 32 32"
        fill="none"
        xmlns="http://www.w3.org/2000/svg"
      >
        <path
          d="M26 16C26 22.8435 18.3441 27.4086 16.4298 28.4431C16.1606 28.5886 15.8394 28.5886 15.5702 28.4431C13.6559 27.4086 6 22.8435 6 16C6 10 10.8453 6 16 6C21.3333 6 26 10 26 16Z"
          stroke={color}
        />
        <circle cx="16" cy="16.0001" r="4.83333" stroke={color} />
      </svg>
    )
  }

  const IcChatLight = ({ size, color }: { size: number; color: string }) => {
    return (
      <svg
        width={`${size}rem`}
        height={`${size}rem`}
        viewBox="0 0 24 24"
        fill="none"
        xmlns="http://www.w3.org/2000/svg"
      >
        <path
          d="M4 12C4 7.58172 7.58172 4 12 4V4C16.4183 4 20 7.58172 20 12V17.0909C20 17.9375 20 18.3608 19.8739 18.6989C19.6712 19.2425 19.2425 19.6712 18.6989 19.8739C18.3608 20 17.9375 20 17.0909 20H12C7.58172 20 4 16.4183 4 12V12Z"
          stroke={color}
        />
        <path
          d="M9 11L15 11"
          stroke={color}
          strokeLinecap="round"
          strokeLinejoin="round"
        />
        <path
          d="M12 15H15"
          stroke={color}
          strokeLinecap="round"
          strokeLinejoin="round"
        />
      </svg>
    )
  }

  const IcUserLight = ({ size, color }: { size: number; color: string }) => {
    return (
      <svg
        width={`${size}rem`}
        height={`${size}rem`}
        viewBox="0 0 32 32"
        fill="none"
        xmlns="http://www.w3.org/2000/svg"
      >
        <circle
          cx="16"
          cy="10.6666"
          r="4.83333"
          stroke={color}
          strokeLinecap="round"
        />
        <path
          d="M6.85847 21.3783C7.7069 19.1009 10.0814 18 12.5117 18H19.4883C21.9186 18 24.2931 19.1009 25.1415 21.3783C25.5255 22.409 25.849 23.6354 25.9594 25.0002C26.004 25.5507 25.5523 26 25 26H7C6.44772 26 5.99602 25.5507 6.04055 25.0002C6.15095 23.6354 6.47449 22.409 6.85847 21.3783Z"
          stroke={color}
          strokeLinecap="round"
        />
      </svg>
    )
  }

  const IcFavorite = ({ size, color }: { size: number; color: string }) => {
    return (
      <svg
        width={`${size}rem`}
        height={`${size}rem`}
        viewBox="0 0 24 24"
        fill="none"
        xmlns="http://www.w3.org/2000/svg"
      >
        <path
          d="M4.45067 13.9082L11.4033 20.4395C11.6428 20.6644 11.7625 20.7769 11.9037 20.8046C11.9673 20.8171 12.0327 20.8171 12.0963 20.8046C12.2375 20.7769 12.3572 20.6644 12.5967 20.4395L19.5493 13.9082C21.5055 12.0706 21.743 9.0466 20.0978 6.92607L19.7885 6.52734C17.8203 3.99058 13.8696 4.41601 12.4867 7.31365C12.2913 7.72296 11.7087 7.72296 11.5133 7.31365C10.1304 4.41601 6.17972 3.99058 4.21154 6.52735L3.90219 6.92607C2.25695 9.0466 2.4945 12.0706 4.45067 13.9082Z"
          stroke={color}
          strokeWidth="2"
        />
      </svg>
    )
  }

  const IcFavoriteFill = ({ size, color }: { size: number; color: string }) => {
    return (
      <svg
        width={`${size}rem`}
        height={`${size}rem`}
        viewBox="0 0 24 24"
        fill="none"
        xmlns="http://www.w3.org/2000/svg"
      >
        <path
          d="M4.45067 13.9082L11.4033 20.4395C11.6428 20.6644 11.7625 20.7769 11.9037 20.8046C11.9673 20.8171 12.0327 20.8171 12.0963 20.8046C12.2375 20.7769 12.3572 20.6644 12.5967 20.4395L19.5493 13.9082C21.5055 12.0706 21.743 9.0466 20.0978 6.92607L19.7885 6.52734C17.8203 3.99058 13.8696 4.41601 12.4867 7.31365C12.2913 7.72296 11.7087 7.72296 11.5133 7.31365C10.1304 4.41601 6.17972 3.99058 4.21154 6.52735L3.90219 6.92607C2.25695 9.0466 2.4945 12.0706 4.45067 13.9082Z"
          fill={color}
          stroke={color}
          strokeWidth="2"
        />
      </svg>
    )
  }

  const IcAddLight = ({ size, color }: { size: number; color: string }) => {
    return (
      <svg
        width={`${size}rem`}
        height={`${size}rem`}
        viewBox="0 0 24 24"
        fill="none"
        xmlns="http://www.w3.org/2000/svg"
      >
        <path d="M12 6L12 18" stroke={color} strokeLinecap="round" />
        <path d="M18 12L6 12" stroke={color} strokeLinecap="round" />
      </svg>
    )
  }

  const IcPrevLight = ({ size, color }: { size: number; color: string }) => {
    return (
      <svg
        width={`${size}rem`}
        height={`${size}rem`}
        viewBox="0 0 24 24"
        fill="none"
        xmlns="http://www.w3.org/2000/svg"
      >
        <path d="M15 6L9 12L15 18" stroke={color} />
      </svg>
    )
  }

  const IcCloseLight = ({ size, color }: { size: number; color: string }) => {
    return (
      <svg
        width={`${size}rem`}
        height={`${size}rem`}
        viewBox="0 0 24 24"
        fill="none"
        xmlns="http://www.w3.org/2000/svg"
      >
        <path
          d="M18 6L6 18"
          stroke={color}
          strokeLinecap="round"
          strokeLinejoin="round"
        />
        <path
          d="M6 6L18 18"
          stroke={color}
          strokeLinecap="round"
          strokeLinejoin="round"
        />
      </svg>
    )
  }

  return {
    IcMenu,
    IcSearch,
    IcBell,
    IcHomeLight,
    IcFavoriteLight,
    IcPinLight,
    IcChatLight,
    IcUserLight,
    IcFavorite,
    IcFavoriteFill,
    IcAddLight,
    IcPrevLight,
    IcCloseLight,
  }
}

export default useIcon
