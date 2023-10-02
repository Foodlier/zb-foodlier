import styled from 'styled-components'
import { breakpoints, palette } from '../../constants/Styles'

export const SearchForm = styled.form`
  position: relative;
  display: flex;
  align-items: center;
  padding: 0 5%;
  margin: 1rem 2rem 0 2rem;

  ${breakpoints.large} {
    margin: 3rem 0 6rem;
    padding: 2.6%;
    border-radius: 1rem;
  }
`

export const SearchInput = styled.input`
  position: absolute;
  left: 0;
  width: 100%;
  flex: 1;
  outline: 0;
  border: 0.1rem solid ${palette.divider};
  border-radius: 1rem;
  padding: 1.8rem 2rem;
  box-shadow: 0.1rem 0.5rem 1.1rem ${palette.shadow};
`

export const SearchButton = styled.button`
  position: absolute;
  right: 2rem;
`