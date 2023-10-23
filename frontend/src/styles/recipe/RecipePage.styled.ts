import styled from 'styled-components'
import {
  breakpoints,
  palette,
  typography,
  weight,
} from '../../constants/Styles'

export const Container = styled.div`
  position: relative;
  width: 100%;
  padding: 0 5%;
  padding-bottom: 20rem;
`

export const WrapRecipeItem = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: space-between;

  ${breakpoints.large} {
    flex-direction: row;
    flex-wrap: wrap;
    justify-content: flex-start;
    width: 103%;
  }
`

export const WrapFilter = styled.div`
  display: flex;
  align-items: center;
  justify-content: flex-end;
  padding: 3rem 0;
`

export const FilterSelect = styled.select`
  border: 0;
`

export const FilterOption = styled.option``

export const WritePage = styled.button`
  position: fixed;
  bottom: calc(10rem + 3%);
  right: 5%;
  padding: 1rem;
  border-radius: 0.5rem;
  background-color: ${palette.main};
  font-size: ${typography.mobile.desc};
  color: ${palette.white};
  font-weight: ${weight.mainTitle};

  ${breakpoints.large} {
    font-size: ${typography.web.desc};
    bottom: 5%;
  }
`
