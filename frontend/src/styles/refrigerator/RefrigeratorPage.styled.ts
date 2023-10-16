import styled from 'styled-components'
import { palette } from '../../constants/Styles'

export const Container = styled.div`
  width: 100%;
`

export const SelectUserList = styled.div`
  display: flex;
  width: 100%;
  padding: 1rem;
`

export const SelectTypeButton = styled.button<{ $isActive: boolean }>`
  flex: 1;
  font-size: 1.6rem;
  padding: 1rem;
  font-weight: ${props => (props.$isActive ? 800 : 400)};
  border-bottom: 1px solid
    ${props => (props.$isActive ? palette.textPrimary : palette.divider)};
`
