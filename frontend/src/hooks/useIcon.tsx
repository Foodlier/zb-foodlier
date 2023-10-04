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

  const IcAddRound = ({ size, color }: { size: number; color: string }) => {
    return (
      <svg
        width={`${size}rem`}
        height={`${size}rem`}
        viewBox="0 0 24 24"
        fill="none"
        xmlns="http://www.w3.org/2000/svg"
      >
        <path
          d="M12 6L12 18"
          stroke={color}
          strokeWidth="2"
          strokeLinecap="round"
        />
        <path
          d="M18 12L6 12"
          stroke={color}
          strokeWidth="2"
          strokeLinecap="round"
        />
      </svg>
    )
  }

  // color 필요 X
  const IcAddRoundDuotone = ({ size }: { size: number }) => {
    return (
      <svg
        width={`${size}rem`}
        height={`${size}rem`}
        viewBox="0 0 40 40"
        fill="none"
        xmlns="http://www.w3.org/2000/svg"
      >
        <circle cx="20" cy="20" r="15" fill="#7E869E" fillOpacity="0.25" />
        <path
          d="M20 13.334L20 26.6673"
          stroke="#222222"
          strokeWidth="1.2"
          strokeLinecap="round"
        />
        <path
          d="M26.666 20L13.3327 20"
          stroke="#222222"
          strokeWidth="1.2"
          strokeLinecap="round"
        />
      </svg>
    )
  }

  const IcFileDockLight = ({
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
        viewBox="0 0 20 20"
        fill="none"
        xmlns="http://www.w3.org/2000/svg"
      >
        <path
          d="M7.08398 10.416L12.9173 10.416"
          stroke={color}
          strokeLinecap="round"
        />
        <path
          d="M7.08398 12.916L10.4173 12.916"
          stroke="#222222"
          strokeLinecap="round"
        />
        <path
          d="M4.58398 5.31602C4.58398 4.47594 4.58398 4.0559 4.74747 3.73503C4.89128 3.45279 5.12076 3.22332 5.403 3.07951C5.72387 2.91602 6.14391 2.91602 6.98398 2.91602H10.2565C10.6234 2.91602 10.8069 2.91602 10.9795 2.95746C11.1326 2.99421 11.2789 3.05481 11.4131 3.13706C11.5645 3.22982 11.6942 3.35953 11.9536 3.61896L14.7144 6.37974C14.9738 6.63917 15.1035 6.76888 15.1963 6.92025C15.2785 7.05446 15.3391 7.20078 15.3759 7.35383C15.4173 7.52647 15.4173 7.70991 15.4173 8.0768V14.6827C15.4173 15.5228 15.4173 15.9428 15.2538 16.2637C15.11 16.5459 14.8805 16.7754 14.5983 16.9192C14.2774 17.0827 13.8574 17.0827 13.0173 17.0827H6.98398C6.14391 17.0827 5.72387 17.0827 5.403 16.9192C5.12076 16.7754 4.89128 16.5459 4.74747 16.2637C4.58398 15.9428 4.58398 15.5228 4.58398 14.6827V5.31602Z"
          stroke={color}
        />
        <path
          d="M10.416 2.91602V5.51602C10.416 6.35609 10.416 6.77613 10.5795 7.097C10.7233 7.37924 10.9528 7.60872 11.235 7.75253C11.5559 7.91602 11.9759 7.91602 12.816 7.91602H15.416"
          stroke={color}
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

  const IcDeskAltLight = ({ size, color }: { size: number; color: string }) => {
    return (
      <svg
        width={`${size}rem`}
        height={`${size}rem`}
        viewBox="0 0 24 24"
        fill="none"
        xmlns="http://www.w3.org/2000/svg"
      >
        <path
          d="M15.5 5C16.9045 5 17.6067 5 18.1111 5.33706C18.3295 5.48298 18.517 5.67048 18.6629 5.88886C19 6.39331 19 7.09554 19 8.5V18C19 19.8856 19 20.8284 18.4142 21.4142C17.8284 22 16.8856 22 15 22H9C7.11438 22 6.17157 22 5.58579 21.4142C5 20.8284 5 19.8856 5 18V8.5C5 7.09554 5 6.39331 5.33706 5.88886C5.48298 5.67048 5.67048 5.48298 5.88886 5.33706C6.39331 5 7.09554 5 8.5 5"
          stroke={color}
        />
        <path
          d="M9 5C9 3.89543 9.89543 3 11 3H13C14.1046 3 15 3.89543 15 5C15 6.10457 14.1046 7 13 7H11C9.89543 7 9 6.10457 9 5Z"
          stroke={color}
        />
        <path d="M9 12L15 12" stroke="#222222" strokeLinecap="round" />
        <path d="M9 16L13 16" stroke="#222222" strokeLinecap="round" />
      </svg>
    )
  }

  const IcImgBoxLight = ({ size, color }: { size: number; color: string }) => {
    return (
      <svg
        width={`${size}rem`}
        height={`${size}rem`}
        viewBox="0 0 40 40"
        fill="none"
        xmlns="http://www.w3.org/2000/svg"
      >
        <path
          d="M5 13C5 9.22876 5 7.34315 6.17157 6.17157C7.34315 5 9.22876 5 13 5H27C30.7712 5 32.6569 5 33.8284 6.17157C35 7.34315 35 9.22876 35 13V27C35 30.7712 35 32.6569 33.8284 33.8284C32.6569 35 30.7712 35 27 35H13C9.22876 35 7.34315 35 6.17157 33.8284C5 32.6569 5 30.7712 5 27V13Z"
          stroke={color}
        />
        <path
          fillRule="evenodd"
          clipRule="evenodd"
          d="M33 25.626L29.8486 22.4746L29.8228 22.4487C29.1781 21.804 28.6585 21.2844 28.1948 20.9307C27.7135 20.5634 27.2342 20.3282 26.6667 20.3282C26.0992 20.3282 25.6199 20.5634 25.1385 20.9307C24.6749 21.2844 24.1552 21.8041 23.5105 22.4488L23.4847 22.4746L21.7353 24.224C21.2505 24.7088 20.926 25.0315 20.6609 25.2283C20.4041 25.419 20.3004 25.417 20.2428 25.4059C20.1852 25.3948 20.0881 25.3581 19.9205 25.0857C19.7475 24.8045 19.566 24.3844 19.2959 23.7542L17.7869 20.2332L17.7697 20.1932L17.7697 20.1932C17.2571 18.9969 16.8499 18.0467 16.4367 17.3751C16.0135 16.6873 15.5221 16.1873 14.7981 16.0479C14.0742 15.9085 13.4323 16.1902 12.7839 16.6717C12.1508 17.1418 11.4198 17.8728 10.4996 18.7931L10.4688 18.8239L7 22.2926V23.7068L11.1759 19.531C12.1339 18.5729 12.8133 17.8954 13.3801 17.4745C13.9386 17.0598 14.2909 16.9686 14.609 17.0299C14.9272 17.0911 15.2204 17.3066 15.5849 17.8991C15.9549 18.5004 16.3341 19.3818 16.8678 20.6271L18.3768 24.1482L18.3925 24.1848C18.6427 24.7686 18.8524 25.258 19.0687 25.6097C19.295 25.9775 19.5902 26.2986 20.0537 26.3878C20.5171 26.4771 20.9104 26.2886 21.2571 26.0312C21.5886 25.785 21.9651 25.4085 22.4142 24.9594L22.4142 24.9593L22.4424 24.9311L24.1918 23.1817C24.8685 22.5051 25.3411 22.0339 25.7451 21.7257C26.1375 21.4263 26.4057 21.3282 26.6667 21.3282C26.9276 21.3282 27.1958 21.4263 27.5882 21.7257C27.9922 22.0339 28.4649 22.5051 29.1415 23.1817L32.9798 27.02L33 26.9997V25.626Z"
          fill={color}
        />
        <circle cx="27.5" cy="12.5" r="2.5" fill={color} />
      </svg>
    )
  }

  const IcExpandRight = ({ size, color }: { size: number; color: string }) => {
    return (
      <svg
        width={`${size}rem`}
        height={`${size}rem`}
        viewBox="0 0 24 24"
        fill="none"
        xmlns="http://www.w3.org/2000/svg"
      >
        <path d="M9 6L15 12L9 18" stroke={color} />
      </svg>
    )
  }

  const IcExportLight = ({ size, color }: { size: number; color: string }) => {
    return (
      <svg
        width={`${size}rem`}
        height={`${size}rem`}
        viewBox="0 0 24 24"
        fill="none"
        xmlns="http://www.w3.org/2000/svg"
      >
        <path
          d="M12 5L11.6464 4.64645L12 4.29289L12.3536 4.64645L12 5ZM12.5 14C12.5 14.2761 12.2761 14.5 12 14.5C11.7239 14.5 11.5 14.2761 11.5 14L12.5 14ZM6.64645 9.64645L11.6464 4.64645L12.3536 5.35355L7.35355 10.3536L6.64645 9.64645ZM12.3536 4.64645L17.3536 9.64645L16.6464 10.3536L11.6464 5.35355L12.3536 4.64645ZM12.5 5L12.5 14L11.5 14L11.5 5L12.5 5Z"
          fill="#222222"
        />
        <path
          d="M5 16L5 17C5 18.1046 5.89543 19 7 19L17 19C18.1046 19 19 18.1046 19 17V16"
          stroke={color}
        />
      </svg>
    )
  }

  const IcTimeLight = ({ size, color }: { size: number; color: string }) => {
    return (
      <svg
        width={`${size}rem`}
        height={`${size}rem`}
        viewBox="0 0 24 24"
        fill="none"
        xmlns="http://www.w3.org/2000/svg"
      >
        <circle cx="12" cy="12" r="8.5" stroke={color} />
        <path
          d="M16.5 12H12.25C12.1119 12 12 11.8881 12 11.75V8.5"
          stroke={color}
          strokeLinecap="round"
        />
      </svg>
    )
  }

  const IcStar = ({ size }: { size: number }) => {
    return (
      <svg
        width={`${size}rem`}
        height={`${size}rem`}
        viewBox="0 0 20 19"
        fill="none"
        xmlns="http://www.w3.org/2000/svg"
      >
        <path
          d="M19.3536 6.69044L13.358 5.8236L10.6778 0.418161C10.6046 0.270163 10.4841 0.150355 10.3354 0.0775313C9.96227 -0.105704 9.50887 0.0469921 9.32232 0.418161L6.64213 5.8236L0.646516 6.69044C0.481218 6.71393 0.330088 6.79145 0.214379 6.90891C0.0744931 7.05195 -0.00259025 7.24438 6.6464e-05 7.44392C0.00272317 7.64346 0.0849026 7.83379 0.228547 7.97309L4.56645 12.1804L3.5416 18.1215C3.51757 18.2597 3.53294 18.4018 3.58598 18.5318C3.63901 18.6617 3.72759 18.7743 3.84166 18.8567C3.95573 18.9391 4.09074 18.9881 4.23137 18.9981C4.37199 19.0081 4.51262 18.9787 4.63729 18.9132L10 16.1083L15.3628 18.9132C15.5092 18.9907 15.6792 19.0165 15.8422 18.9883C16.2531 18.9179 16.5293 18.5302 16.4585 18.1215L15.4336 12.1804L19.7715 7.97309C19.8896 7.85798 19.9675 7.70763 19.9912 7.54319C20.0549 7.13208 19.7668 6.75152 19.3536 6.69044Z"
          fill="#FFE921"
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
    IcAddRound,
    IcAddRoundDuotone,
    IcFileDockLight,
    IcAddLight,
    IcPrevLight,
    IcCloseLight,
    IcDeskAltLight,
    IcImgBoxLight,
    IcExpandRight,
    IcExportLight,
    IcTimeLight,
    IcStar,
  }
}

export default useIcon
