import styled from 'styled-components'
import { breakpoints, palette } from '../../constants/Styles'

export const Container = styled.div`
  position: relative;
  width: 100%;
  padding: 0 5%;
`

export const WrapRecipeItem = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: space-between;

  ${breakpoints.large} {
    flex-direction: row;
  }
`

export const WrapFilter = styled.div`
  display: flex;
  align-items: center;
  justify-content: flex-end;
  padding: 3rem 0;
`

export const FilterSelect = styled.select`
  border: 0px;
`

export const FilterOption = styled.option``

export const WritePage = styled.button`
  position: fixed;
  bottom: calc(10rem + 3%);
  right: 5%;
  padding: 1rem;
  border-radius: 1rem;
  background-color: ${palette.main};
  color: white;

  ${breakpoints.large} {
    bottom: 5%;
  }
`
