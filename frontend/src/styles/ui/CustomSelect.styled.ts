import styled from 'styled-components'
import { breakpoints, palette } from '../../constants/Styles'

export const Container = styled.div`
  position: relative;
  display: flex;
  align-items: center;
  width: 30%;
  /* width: 20rem; */
  min-height: 5rem;
  max-height: 5rem;
  border: 1px solid ${palette.divider};
  border-radius: 1.2rem;
  padding: 0 1rem;

  background-color: ${palette.white};
  box-shadow: 0px 4px 4px ${palette.shadow};
  cursor: pointer;
  ${breakpoints.large} {
    max-width: 20%;
  }
`

export const Label = styled.label``

export const List = styled.ul<{ $show: boolean }>`
  display: ${props => (props.$show ? 'block' : 'none')};
  position: absolute;
  top: 5rem;
  left: 0;
  width: 100%;
  overflow: hidden;
  padding: 0;
  border: 1px solid ${palette.divider};
  border-radius: 8px;
  background-color: ${palette.white};
  color: ${palette.textPrimary};
  box-shadow: inherit;
`

export const ListItem = styled.li`
  font-size: 1.4rem;
  padding: 1rem;
  border-bottom: 1px solid ${palette.divider};
`
