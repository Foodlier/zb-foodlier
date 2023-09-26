import React from 'react'
import styled, { css } from 'styled-components'
import { palette } from '../../constants/Styles'

import '../../reset.css'

const colorVariables = {
  main: 'var(--color-main)',
  textPrimary: 'var(--color-text-primary)',
  disable: 'var(--color-text-disable)',
}

interface CommonButtonProps {
  children: React.ReactNode
  color?: keyof typeof colorVariables
  size?: 'small' | 'medium' | 'large'
  onClick?: () => void
}

const colorStyles = css<CommonButtonProps>`
  /* 색상 */
  ${({ color }) => {
    const bgColor = colorVariables[color || 'main']
    return css`
      background-color: ${bgColor};
      color: ${palette.white};
      &:hover {
        opacity: 0.9;
      }
    `
  }}
`

const sizeStyles = css<CommonButtonProps>`
  /* 크기 */
  ${({ size }) =>
    size === 'large' &&
    css`
      height: 4.25rem;
      padding: 0 2.3rem;
      font-size: 1.6rem;
    `}
  ${({ size }) =>
    size === 'medium' &&
    css`
      height: 3.75rem;
      padding: 0 1.6rem;
      font-size: 1.4rem;
    `}
  ${({ size }) =>
    size === 'small' &&
    css`
      height: 2.75rem;
      font-size: 1.2rem;
    `}
`

const StyledButton = styled.button<CommonButtonProps>`
  /* 공통 스타일 */
  display: inline-flex;
  align-items: center;
  outline: none;
  border: none;
  border-radius: 0.4rem;
  color: ${palette.white};
  font-weight: 600;
  cursor: pointer;
  padding: 0 1.2rem;
  font-size: 1.6rem;

  /* 기타 */
  & + & {
    margin-left: 1rem;
  }

  background-color: ${palette.main};
  color: ${palette.white};

  ${sizeStyles}
  ${colorStyles}
`

const CommonButton: React.FC<CommonButtonProps> = ({
  children,
  color,
  size,
  onClick,
}) => {
  return (
    <StyledButton color={color} size={size} onClick={onClick}>
      {children}
    </StyledButton>
  )
}

CommonButton.defaultProps = {
  color: 'main',
  size: 'medium',
  onClick: () => {},
}

export default CommonButton
