import styled from 'styled-components'
import { palette } from '../../constants/Styles'

export const Container = styled.div`
  width: 100%;
`

export const Map = styled.div`
  width: 100%;
  height: 440px;
  background-color: teal;
`

export const SelectUserList = styled.div`
  width: 100%;
  padding: 20px 20px 0;
`

export const SelectTypeButton = styled.button`
  width: 50%;
  height: 50px;
  border-bottom: 1px solid ${palette.divider};
  font-size: 2rem;
  &:focus {
    font-weight: bold;
    border-bottom: 1px solid #000;
  }
`
