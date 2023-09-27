import styled from 'styled-components'
import { palette } from '../../constants/Styles'

export const ImageButton = styled.button<{ $size: number }>`
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  width: ${props => props.$size || 0}rem;
  height: ${props => props.$size || 0}rem;
  border-radius: 1rem;
  background-color: #f3f3f3;
  border: 1px solid ${palette.divider};
`

export const SubText = styled.span`
  font-size: 1.2rem;
  color: ${palette.textSecondary};
  margin-top: 2rem;
`
