import styled from 'styled-components'
import { breakpoints, palette, typography } from '../../constants/Styles'

export const Label = styled.label<{ $size: number }>`
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  width: ${props => props.$size || 0}rem;
  height: ${props => props.$size || 0}rem;
  min-width: ${props => props.$size || 0}rem;
  min-height: ${props => props.$size || 0}rem;

  border-radius: 1rem;
  background-color: #f3f3f3;
  border: 1px solid ${palette.divider};
  cursor: pointer;
`

export const ImageButton = styled.input<{ $size: number }>`
  display: none;
`

export const SubText = styled.span`
  font-size: ${typography.mobile.desc};
  color: ${palette.textSecondary};
  margin-top: 2rem;

  ${breakpoints.large} {
    font-size: ${typography.web.desc};
  }
`

export const EmptyImage = styled.button<{ $size: number }>`
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  width: ${props => props.$size || 0}rem;
  height: ${props => props.$size || 0}rem;
  min-width: ${props => props.$size || 0}rem;
  min-height: ${props => props.$size || 0}rem;
  object-fit: cover;
  border-radius: 1rem;
  background-color: #f3f3f3;
  border: 1px solid ${palette.divider};
`

export const Image = styled.img<{ $size: number }>`
  width: ${props => props.$size || 0}rem;
  height: ${props => props.$size || 0}rem;
  min-width: ${props => props.$size || 0}rem;
  min-height: ${props => props.$size || 0}rem;
  object-fit: cover;
  border: 1px solid ${palette.divider};
  border-radius: 1rem;
`
