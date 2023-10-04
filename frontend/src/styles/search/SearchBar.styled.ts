import styled from 'styled-components'
import { breakpoints, palette } from '../../constants/Styles'

export const SearchForm = styled.form`
  padding: 10% 5%;
  margin: 0 2rem 1rem 2rem;
  position: relative;
  display: flex;
  align-items: center;

  ${breakpoints.large} {
    margin: 3rem 0 3rem;
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
export const DropdownContent = styled.ul`
  /* position: fixed;
  left: 50%;
  transform: translateX(-50%);
  top: 17rem; */
  z-index: 1000;
  display: flex;
  flex-direction: column;
  width: 100%;
  max-width: 90%;

  margin: 0 auto;
  flex-wrap: nowrap;
  height: auto;
  box-shadow:
    0 1px 3px rgba(0, 0, 0, 0.12),
    0 1px 2px rgba(0, 0, 0, 0.24);
  font-size: 1rem;
  overflow-y: auto;
  overflow-x: hidden;
  background-color: white;
  border-radius: 0.5rem;
  padding: 2.6%;

  ${breakpoints.large} {
    max-width: 100%;
  }

  & > li {
    position: relative;
    display: flex;
    flex-shrink: 0;
    flex-direction: column;
    flex-wrap: wrap;
    align-items: stretch;
    padding: 0.5rem 0;
  }
`
export const SearchedItem = styled.button`
  text-align: left;
  color: ${palette.textPrimary};
  font-size: 1.6rem;
  position: relative;
  display: flex;
  flex-shrink: 0;
  flex-direction: column;
  flex-wrap: wrap;
  align-items: stretch;

  &.selected {
    font-weight: 600;
  }
`
