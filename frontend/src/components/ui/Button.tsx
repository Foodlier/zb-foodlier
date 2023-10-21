import React from 'react'
import styled, { css } from 'styled-components'
import '../../reset.css'
import { palette } from '../../constants/Styles'

interface CommonButtonProps {
  children: React.ReactNode
  color?: keyof typeof palette
  size?: 'small' | 'medium' | 'large'
  border?: 'border' | 'borderNone'
  onClick?: () => void
}

const colorStyles = css<CommonButtonProps>`
  ${({ color }) =>
    color === 'main' &&
    css`
      background-color: ${palette.main};
      color: ${palette.white};
    `}
  ${({ color }) =>
    color === 'divider' &&
    css`
      background-color: ${palette.divider};
      color: ${palette.textPrimary};
    `}
    ${({ color }) =>
    color === 'white' &&
    css`
      background-color: ${palette.white};
      color: ${palette.white};
    `}
`

const borderStyles = css<CommonButtonProps>`
  ${({ border }) =>
    border === 'border' &&
    css`
      border: 0.1rem solid ${palette.main};
      background-color: ${palette.white};
      color: ${palette.main};
    `}
  ${({ border }) =>
    border === 'borderNone' &&
    css`
      border: none;
    `}
`

const sizeStyles = css<CommonButtonProps>`
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
  font-weight: bold;
  cursor: pointer;
  padding: 0 1.2rem;
  font-size: 1.6rem;

  /* 기타 */
  & + & {
    margin-left: 1rem;
  }

  background-color: var(--color-main);
  color: var(--color-white);

  ${sizeStyles}
  ${colorStyles}
  ${borderStyles}
`

const CommonButton: React.FC<CommonButtonProps> = ({
  children,
  color,
  size,
  border,
  onClick,
}) => {
  return (
    <StyledButton color={color} size={size} border={border} onClick={onClick}>
      {children}
    </StyledButton>
  )
}

CommonButton.defaultProps = {
  color: 'main',
  size: 'medium',
  border: 'borderNone',
  onClick: () => {},
}

export default CommonButton
