import styled from 'styled-components'
import {
  breakpoints,
  palette,
  typography,
  zindex,
} from '../../constants/Styles'

export const Container = styled.div`
  position: relative;
  display: flex;
  align-items: center;
  width: 30%;
  min-height: 5rem;
  max-height: 5rem;
  border: 1px solid ${palette.divider};
  border-radius: 1rem;
  padding: 0 1rem;
  background-color: ${palette.white};
  box-shadow: 0 0.4rem 0.4rem ${palette.shadow};
  cursor: pointer;
  z-index: ${zindex.header - 1};
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
  border: 0.1rem solid ${palette.divider};
  border-radius: 1rem;
  background-color: ${palette.white};
  color: ${palette.textPrimary};
  box-shadow: inherit;
`

export const ListItem = styled.li`
  font-size: ${typography.mobile.desc};
  padding: 1rem;
  border-bottom: 1px solid ${palette.divider};

  ${breakpoints.large} {
    font-size: ${typography.web.desc};
  }
`
